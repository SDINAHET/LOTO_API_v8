package com.fdjloto.api.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIT extends BaseIntegrationTest {

    @Test
    void shouldRegisterUser() throws Exception {

        String email = "user" + System.currentTimeMillis() + "@loto.com";
        String password = "password123";

        String payload = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Test",
          "lastName":"User"
        }
        """.formatted(email, password);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void shouldLoginUser() throws Exception {

        String email = "login" + System.currentTimeMillis() + "@loto.com";
        String password = "password123";

        String register = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Test",
          "lastName":"User"
        }
        """.formatted(email, password);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(register));

        String login = """
        {
          "email":"%s",
          "password":"%s"
        }
        """.formatted(email, password);

        mockMvc.perform(post("/api/auth/login3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(login))
                .andExpect(status().isOk());
    }


    @Test
    void shouldFailLoginWithWrongPassword() throws Exception {

        String email = "wrongpass" + System.currentTimeMillis() + "@loto.com";
        String correctPassword = "password123";

        // 🔹 1. Register valid user
        String registerPayload = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Test",
          "lastName":"User"
        }
        """.formatted(email, correctPassword);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerPayload))
                .andExpect(status().isOk());

        // 🔹 2. Login with WRONG password (>= 6 chars)
        String loginPayload = """
        {
          "email":"%s",
          "password":"wrong12"
        }
        """.formatted(email);

        mockMvc.perform(post("/api/auth/login3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldFailRegisterWithInvalidPayload() throws Exception {

        String payload = """
        {
          "email":"invalid-email",
          "password":"123",
          "firstName":"",
          "lastName":""
        }
        """;

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
                // .andExpect(status().isUnauthorized());
                // .andExpect(status().isInternalServerError()); // ✅ 500
    }

    @Test
    void shouldFailLoginWithInvalidEmailFormat() throws Exception {

        String payload = """
        {
          "email":"invalid-email",
          "password":"password123"
        }
        """;

        mockMvc.perform(post("/api/auth/login3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                // .andExpect(status().isBadRequest());
                .andExpect(status().isUnauthorized()); // ✅ 401 cohérent avec ton système
    }
}
