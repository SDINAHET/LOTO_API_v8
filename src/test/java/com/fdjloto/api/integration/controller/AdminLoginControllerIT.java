package com.fdjloto.api.integration.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // 🔒 IMPORTANT pour ne pas toucher la prod
class AdminLoginControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldForwardToAdminLoginHtml() throws Exception {

        mockMvc.perform(get("/admin-login"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/admin-login.html"));
    }
}
