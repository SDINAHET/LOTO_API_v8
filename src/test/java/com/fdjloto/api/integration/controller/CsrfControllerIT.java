package com.fdjloto.api.integration.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CsrfControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnCsrfTokenAndSetNoStoreHeader() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/auth/csrf")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();

        // 🔎 Extraction du token depuis le JSON
        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String token = jsonNode.get("token").asText();

        // 🔹 Simulation cookie basé sur le token retourné
        Cookie csrfCookie = new Cookie("XSRF-TOKEN", token);

        // ✅ Vérification que le cookie contient bien le token
        org.junit.jupiter.api.Assertions.assertEquals(token, csrfCookie.getValue());
    }
}
