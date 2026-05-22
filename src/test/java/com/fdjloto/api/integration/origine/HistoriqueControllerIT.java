package com.fdjloto.api.integration;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class HistoriqueControllerIT extends BaseIntegrationTest {

    @Test
    void shouldGetLast20Draws() throws Exception {

        mockMvc.perform(get("/api/historique/last20"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetDrawByDate() throws Exception {

        mockMvc.perform(get("/tirage/2026-03-02"))
                .andExpect(status().isOk())
                .andExpect(view().name("tirage-date"));
    }

    @Test
    void shouldReturn404ForInvalidDate() throws Exception {

        mockMvc.perform(get("/api/historique/1900-01-01"))
                .andExpect(status().isNotFound());
    }

}
