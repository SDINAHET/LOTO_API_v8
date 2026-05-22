package com.fdjloto.api.integration.controller;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomErrorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // ==============================
    // 401
    // ==============================

    @Test
    void shouldForwardTo401Page() throws Exception {
        mockMvc.perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 401))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/errors/401.html"));
    }

    // ==============================
    // 403
    // ==============================

    @Test
    void shouldForwardTo403Page() throws Exception {
        mockMvc.perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 403))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/errors/403.html"));
    }

    // ==============================
    // 404
    // ==============================

    @Test
    void shouldForwardTo404Page() throws Exception {
        mockMvc.perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/errors/404.html"));
    }

    // ==============================
    // 500
    // ==============================

    @Test
    void shouldForwardTo500Page() throws Exception {
        mockMvc.perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 500))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/errors/500.html"));
    }

    // ==============================
    // null status -> fallback 500
    // ==============================

    @Test
    void shouldFallbackTo500WhenStatusIsNull() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/errors/500.html"));
    }
}
