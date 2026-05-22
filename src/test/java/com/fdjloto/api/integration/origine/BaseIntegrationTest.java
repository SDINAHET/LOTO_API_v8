package com.fdjloto.api.integration;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    protected Cookie jwtCookie;

    @BeforeEach
    void loginUser() throws Exception {

        String email = "test" + System.currentTimeMillis() + "@loto.com";
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

        MvcResult result = mockMvc.perform(post("/api/auth/login3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(login))
                .andReturn();

        jwtCookie = result.getResponse().getCookie("jwtToken");
    }
}
