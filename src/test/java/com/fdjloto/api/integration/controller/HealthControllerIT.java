package com.fdjloto.api.integration.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HealthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // ============================================================
    // 1️⃣ HEALTH CHECK → 200 OK
    // ============================================================

    @Test
    @DisplayName("Health endpoint should return 200 OK with plain text 'OK'")
    void shouldReturnHealthStatusOk() throws Exception {

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("OK"));
    }

    // ============================================================
    // 2️⃣ METHOD NOT ALLOWED → 405
    // ============================================================

    @Test
    @DisplayName("POST on health endpoint should return 405")
    void shouldReturn405ForWrongMethod() throws Exception {

        mockMvc.perform(get("/api/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // GET is allowed

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/health")
        )
                .andExpect(status().isMethodNotAllowed());
    }

    // ============================================================
    // 3️⃣ UNKNOWN URL → 404
    // ============================================================

    // @Test
    // @DisplayName("Unknown health URL should return 404")
    // void shouldReturn404ForUnknownHealthUrl() throws Exception {

    //     mockMvc.perform(get("/api/health-unknown"))
    //             .andExpect(status().isNotFound());
    // }
}
