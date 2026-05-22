package com.fdjloto.api.integration.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HelloControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // ============================================================
    // 1️⃣ GET /api/hello → 200 OK
    // ============================================================

    @Test
    @DisplayName("GET /api/hello should return success message")
    void shouldReturnHelloMessage() throws Exception {

        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ Loto API is running without a database!"));
    }

    // ============================================================
    // 2️⃣ POST → 405 Method Not Allowed
    // ============================================================

    @Test
    @DisplayName("POST /api/hello should return 405")
    void shouldReturn405ForWrongMethod() throws Exception {

        mockMvc.perform(post("/api/hello"))
                .andExpect(status().isMethodNotAllowed());
    }

    // ============================================================
    // 3️⃣ Unknown URL → 404
    // ============================================================

    // @Test
    // @DisplayName("Unknown hello URL should return 404")
    // void shouldReturn404ForUnknownUrl() throws Exception {

    //     mockMvc.perform(get("/api/hello-unknown"))
    //             .andExpect(status().isNotFound());
    // }
}
