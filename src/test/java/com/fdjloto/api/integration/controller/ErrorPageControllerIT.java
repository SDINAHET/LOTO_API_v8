package com.fdjloto.api.integration.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ErrorPageControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // ============================================================
    // 401
    // ============================================================

    @Test
    @DisplayName("Should forward to custom 401 page")
    void shouldReturn401Page() throws Exception {

        mockMvc.perform(get("/401"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/errors/401.html"));
    }

    // ============================================================
    // 403
    // ============================================================

    @Test
    @DisplayName("Should forward to custom 403 page")
    void shouldReturn403Page() throws Exception {

        mockMvc.perform(get("/403"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/errors/403.html"));
    }

    // ============================================================
    // 404
    // ============================================================

    @Test
    @DisplayName("Should forward to custom 404 page")
    void shouldReturn404Page() throws Exception {

        mockMvc.perform(get("/404"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/errors/404.html"));
    }

    // ============================================================
    // 500
    // ============================================================

    @Test
    @DisplayName("Should forward to custom 500 page")
    void shouldReturn500Page() throws Exception {

        mockMvc.perform(get("/500"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/errors/500.html"));
    }
}
