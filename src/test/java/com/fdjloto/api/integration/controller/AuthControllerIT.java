package com.fdjloto.api.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.Cookie;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional // 🔥 rollback automatique après chaque test
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private static final String PASSWORD = "password123";

    // ============================================================
    // REGISTER + LOGIN helper
    // ============================================================

    private String uniqueEmail() {
        return "auth" + System.nanoTime() + "@test.com";
    }

    private Cookie[] registerAndLogin(String email) throws Exception {

        String registerPayload = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Test",
          "lastName":"User"
        }
        """.formatted(email, PASSWORD);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerPayload))
                .andExpect(status().isOk());

        String loginPayload = """
        {
          "email":"%s",
          "password":"%s"
        }
        """.formatted(email, PASSWORD);

        var result = mockMvc.perform(post("/api/auth/login3")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        return result.getResponse().getCookies();
    }

    // ============================================================
    // REGISTER
    // ============================================================

    @Test
    void shouldRegisterUser() throws Exception {

        String email = uniqueEmail();

        String payload = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Test",
          "lastName":"User"
        }
        """.formatted(email, PASSWORD);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk());
    }

    // ============================================================
    // LOGIN
    // ============================================================

    @Test
    void shouldLoginWithCookies() throws Exception {

        String email = uniqueEmail();
        Cookie[] cookies = registerAndLogin(email);

        mockMvc.perform(get("/api/auth/me")
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
    }

    // ============================================================
    // TOKEN STATUS
    // ============================================================

    @Test
    void shouldReturnTokenStatus() throws Exception {

        String email = uniqueEmail();
        Cookie[] cookies = registerAndLogin(email);

        mockMvc.perform(get("/api/auth/token")
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.present").value(true))
                .andExpect(jsonPath("$.valid").value(true));
    }

    // ============================================================
    // REFRESH
    // ============================================================

    @Test
    void shouldRefreshToken() throws Exception {

        String email = uniqueEmail();
        Cookie[] cookies = registerAndLogin(email);

        mockMvc.perform(post("/api/auth/refresh")
                        .cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwtToken"))
                .andExpect(cookie().exists("refreshToken"));
    }

    // ============================================================
    // LOGOUT
    // ============================================================

    @Test
    void shouldLogoutSuccessfully() throws Exception {

        String email = uniqueEmail();
        Cookie[] cookies = registerAndLogin(email);

        mockMvc.perform(post("/api/auth/logout")
                        .with(csrf())
                        .cookie(cookies))
                .andExpect(status().isOk());
    }

    // ============================================================
    // LOGIN FAILURE
    // ============================================================

    @Test
    void shouldFailLogin() throws Exception {

        String payload = """
        {
          "email":"unknown@test.com",
          "password":"badpassword"
        }
        """;

        mockMvc.perform(post("/api/auth/login3")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isUnauthorized());
    }

    // ============================================================
    // PING
    // ============================================================

    @Test
    void shouldReturnPing() throws Exception {

        mockMvc.perform(get("/api/auth/ping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("AuthController OK"));
    }
}
