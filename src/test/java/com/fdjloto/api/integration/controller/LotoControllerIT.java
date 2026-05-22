package com.fdjloto.api.integration.controller;

import com.fdjloto.api.service.LotoScraperService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LotoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // 🔥 On mock le service pour éviter le vrai scraping
    @MockBean
    private LotoScraperService lotoScraperService;

    // ============================================================
    // 1️⃣ SCRAPE → 200 OK
    // ============================================================

    @Test
    @DisplayName("Should trigger scraping and return success message")
    void shouldTriggerScraping() throws Exception {

        mockMvc.perform(
                get("/api/loto/scrape")
        )
        .andExpect(status().isOk())
        .andExpect(content().string("Scraping successfully started!"));

        // ✅ Vérifie que le service a été appelé
        verify(lotoScraperService).scrapeData();
    }

    // ============================================================
    // 2️⃣ WRONG METHOD → 405
    // ============================================================

    @Test
    @DisplayName("POST should return 405 Method Not Allowed")
    void shouldReturn405ForWrongMethod() throws Exception {

        mockMvc.perform(
                post("/api/loto/scrape")
        )
        .andExpect(status().isMethodNotAllowed());
    }

    // ============================================================
    // 3️⃣ UNKNOWN URL → 404
    // ============================================================

    // @Test
    // @DisplayName("Unknown URL should return 404")
    // void shouldReturn404ForUnknownUrl() throws Exception {

    //     mockMvc.perform(
    //             get("/api/loto/unknown")
    //     )
    //     .andExpect(status().isNotFound());
    // }
}
