package com.fdjloto.api.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TicketControllerIT extends BaseIntegrationTest {

    @Test
    void shouldCreateTicket() throws Exception {

        String payload = """
        {
        "numbers":"1,2,3,4,5",
        "chanceNumber":6,
        "drawDate":"2026-03-08"
        }
        """;

        mockMvc.perform(post("/api/tickets")
                .cookie(jwtCookie)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetTickets() throws Exception {

        mockMvc.perform(get("/api/tickets")
                .cookie(jwtCookie))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn401WhenNoToken() throws Exception {

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturn404WhenTicketNotFound() throws Exception {

        mockMvc.perform(get("/api/tickets/unknown-id")
                .cookie(jwtCookie))
                .andExpect(status().isNotFound());
    }
}
