package com.fdjloto.api.integration.controller;

import com.fdjloto.api.dto.AnalyticsEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnalyticsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ============================================================
    // 1️⃣ 200 OK - basic event
    // ============================================================

    @Test
    @DisplayName("Should log analytics event successfully")
    void shouldLogEventSuccessfully() throws Exception {

        AnalyticsEvent event = new AnalyticsEvent();
        event.setEventType("CLICK");
        event.setPage("/home");
        event.setExtra(Map.of("button", "play"));

        mockMvc.perform(post("/api/analytics/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk());
    }

    // ============================================================
    // 2️⃣ X-Forwarded-For branch
    // ============================================================

    @Test
    @DisplayName("Should extract client IP from X-Forwarded-For")
    void shouldExtractIpFromForwardedHeader() throws Exception {

        AnalyticsEvent event = new AnalyticsEvent();
        event.setEventType("VIEW");
        event.setPage("/dashboard");
        event.setExtra(Map.of());

        mockMvc.perform(post("/api/analytics/event")
                        .header("X-Forwarded-For", "192.168.1.100, 10.0.0.1")
                        .header("User-Agent", "JUnit-Test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk());
    }

    // ============================================================
    // 3️⃣ RemoteAddr fallback branch
    // ============================================================

    @Test
    @DisplayName("Should fallback to remote address")
    void shouldUseRemoteAddrIfNoForwardedHeader() throws Exception {

        AnalyticsEvent event = new AnalyticsEvent();
        event.setEventType("NAVIGATION");
        event.setPage("/profile");
        event.setExtra(Map.of());

        mockMvc.perform(post("/api/analytics/event")
                        .with(request -> {
                            request.setRemoteAddr("127.0.0.1");
                            return request;
                        })
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isOk());
    }

    // ============================================================
    // 4️⃣ 400 - Invalid JSON
    // ============================================================

    @Test
    @DisplayName("Should return 400 for invalid JSON")
    void shouldReturn400ForInvalidPayload() throws Exception {

        String invalidJson = "{ invalid-json }";

        mockMvc.perform(post("/api/analytics/event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
