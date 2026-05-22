package com.fdjloto.api.controller.admin;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AdminDebugControllerIT {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test ping avec ADMIN + cookie JWT
     */
    @Test
    @WithMockUser(username = "admin@fdjloto.fr", roles = {"ADMIN"})
    void pingShouldReturnStatusUp() throws Exception {

        mockMvc.perform(get("/api/admin/ping")
                        .cookie(new Cookie("jwtToken", "fake-jwt"))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.jwtCookiePresent").value(true))
                .andExpect(jsonPath("$.user").value("admin@fdjloto.fr"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.now", notNullValue()));
    }

    /**
     * ping sans cookie
     */
    @Test
    @WithMockUser(username = "admin@fdjloto.fr", roles = {"ADMIN"})
    void pingWithoutCookieShouldReturnFalse() throws Exception {

        mockMvc.perform(get("/api/admin/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtCookiePresent").value(false));
    }

    /**
     * auth-info
     */
    @Test
    @WithMockUser(username = "admin@fdjloto.fr", roles = {"ADMIN"})
    void authInfoShouldReturnAuthenticationData() throws Exception {

        mockMvc.perform(get("/api/admin/auth-info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated").value(true))
                .andExpect(jsonPath("$.user").value("admin@fdjloto.fr"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.authClass", notNullValue()))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }

    /**
     * health endpoint
     */
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void healthShouldReturnServerMetrics() throws Exception {

        mockMvc.perform(get("/api/admin/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.serverTime", notNullValue()))
                .andExpect(jsonPath("$.memoryUsedMB", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.memoryMaxMB", greaterThan(0)))
                .andExpect(jsonPath("$.processors", greaterThan(0)));
    }

    /**
     * runtime endpoint
     */
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void runtimeShouldReturnSystemInfo() throws Exception {

        mockMvc.perform(get("/api/admin/runtime"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.javaVersion", notNullValue()))
                .andExpect(jsonPath("$.javaVendor", notNullValue()))
                .andExpect(jsonPath("$.osName", notNullValue()))
                .andExpect(jsonPath("$.osArch", notNullValue()))
                .andExpect(jsonPath("$.osVersion", notNullValue()))
                .andExpect(jsonPath("$.timezone", notNullValue()));
    }

    /**
     * request-context avec cookie
     */
    // @Test
    // @WithMockUser(roles = {"ADMIN"})
    // void requestContextShouldReturnRequestData() throws Exception {

    //     mockMvc.perform(get("/api/admin/request-context")
    //                     .cookie(new Cookie("jwtToken", "fake-jwt"))
    //                     .header("User-Agent", "JUnit"))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.cookiePresent").value(true))
    //             .andExpect(jsonPath("$.method").value("GET"))
    //             .andExpect(jsonPath("$.uri").value("/api/admin/request-context"))
    //             .andExpect(jsonPath("$.remoteAddr", notNullValue()))
    //             .andExpect(jsonPath("$.userAgent").value("JUnit"));
    // }
	@Test
	@WithMockUser(roles = {"ADMIN"})
	void requestContextShouldReturnRequestData() throws Exception {

		mockMvc.perform(get("/api/admin/request-context")
						.cookie(new Cookie("jwtToken", "fake-jwt"))
						.header("User-Agent", "JUnit")
						.header("Referer", "http://localhost/test"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.cookiePresent").value(true))
				.andExpect(jsonPath("$.method").value("GET"))
				.andExpect(jsonPath("$.uri").value("/api/admin/request-context"))
				.andExpect(jsonPath("$.remoteAddr", notNullValue()))
				.andExpect(jsonPath("$.userAgent").value("JUnit"))
				.andExpect(jsonPath("$.referer").value("http://localhost/test"));
	}

    /**
     * cookies endpoint avec cookies
     */
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void cookiesShouldReturnCookieList() throws Exception {

        Cookie cookie = new Cookie("jwtToken", "fake");

        mockMvc.perform(get("/api/admin/cookies")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cookiePresent").value(true))
                .andExpect(jsonPath("$.cookies", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.cookies[0].name").value("jwtToken"));
    }

    /**
     * cookies endpoint sans cookies
     */
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void cookiesShouldReturnEmptyListWhenNoCookies() throws Exception {

        mockMvc.perform(get("/api/admin/cookies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cookies", hasSize(0)));
    }

    /**
     * uptime endpoint
     */
    @Test
    @WithMockUser(roles = {"ADMIN"})
    void uptimeShouldReturnApplicationUptime() throws Exception {

        mockMvc.perform(get("/api/admin/uptime"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uptimeSeconds", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.uptimeMinutes", greaterThanOrEqualTo(0)))
                .andExpect(jsonPath("$.uptimeHours", greaterThanOrEqualTo(0)));
    }
}
