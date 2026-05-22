package com.fdjloto.api.integration;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PublicUserWorkflowIT {

    @Autowired
    private MockMvc mockMvc;

    private static Cookie jwtCookie;

    private static final String EMAIL =
            "testuser" + System.currentTimeMillis() + "@loto.com";

    private static final String PASSWORD = "password123";

    // -------------------------------------------------------
    // 1 REGISTER
    // -------------------------------------------------------

    @Test
    @Order(1)
    void register_user() throws Exception {

        String body = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Test",
          "lastName":"User"
        }
        """.formatted(EMAIL, PASSWORD);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------
    // 2 LOGIN
    // -------------------------------------------------------

    @Test
    @Order(2)
    void login_should_return_jwt_cookie() throws Exception {

        String body = """
        {
          "email":"%s",
          "password":"%s"
        }
        """.formatted(EMAIL, PASSWORD);

        MvcResult result = mockMvc.perform(post("/api/auth/login3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwtToken"))
                .andReturn();

        jwtCookie = result.getResponse().getCookie("jwtToken");
    }

    // -------------------------------------------------------
    // 3 CREATE TICKET
    // -------------------------------------------------------

	@Test
	@Order(3)
	void create_ticket() throws Exception {

		String ticket = """
		{
        "numbers":"1-2-3-4-5",
		"chanceNumber":"6",
		"drawDate":"2024-06-10"
		}
		""";

		mockMvc.perform(post("/api/tickets")
				.cookie(jwtCookie)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(ticket))
				.andExpect(status().isOk());
	}

    // -------------------------------------------------------
    // 4 CALCULATE GAINS
    // -------------------------------------------------------

    @Test
    @Order(4)
    void calculate_gains() throws Exception {

        mockMvc.perform(get("/api/gains/calculate")
                .cookie(jwtCookie))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------
    // 5 LOGOUT
    // -------------------------------------------------------

    @Test
    @Order(5)
    void logout() throws Exception {

        mockMvc.perform(post("/api/auth/logout")
                .cookie(jwtCookie)
                .with(csrf()))
                .andExpect(status().isOk());
    }

    // -------------------------------------------------------
    // 6 LOGIN AGAIN
    // -------------------------------------------------------

    @Test
    @Order(6)
    void login_again() throws Exception {

    	Thread.sleep(1000); // éviter collision refresh token

        String body = """
        {
          "email":"%s",
          "password":"%s"
        }
        """.formatted(EMAIL, PASSWORD);

        mockMvc.perform(post("/api/auth/login3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }
}
