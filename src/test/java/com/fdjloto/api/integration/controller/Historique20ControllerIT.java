package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.Historique20Result;
import com.fdjloto.api.service.Historique20Service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class Historique20ControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Historique20Service historique20Service;

    // ============================================================
    // 1️⃣ SUCCESS → 200 OK
    // ============================================================

    @Test
    @DisplayName("GET /api/historique/last20 should return list of results")
    void shouldReturnLast20Results() throws Exception {

        Historique20Result mockResult = new Historique20Result();
        mockResult.setId("abc123");
        mockResult.setDateDeTirage(new Date());
        mockResult.setBoule1(1);
        mockResult.setBoule2(2);
        mockResult.setBoule3(3);
        mockResult.setBoule4(4);
        mockResult.setBoule5(5);
        mockResult.setNumeroChance(7);

        when(historique20Service.getLast20Results())
                .thenReturn(List.of(mockResult));

        mockMvc.perform(get("/api/historique/last20"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].id").value("abc123"))
                .andExpect(jsonPath("$[0].boule1").value(1))
                .andExpect(jsonPath("$[0].numeroChance").value(7));
    }

    // ============================================================
    // 2️⃣ EMPTY LIST → 200 OK
    // ============================================================

    @Test
    @DisplayName("GET /api/historique/last20 should return empty list")
    void shouldReturnEmptyList() throws Exception {

        when(historique20Service.getLast20Results())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/historique/last20"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // ============================================================
    // 3️⃣ WRONG METHOD → 405
    // ============================================================

    @Test
    @DisplayName("POST should return 405")
    void shouldReturn405ForWrongMethod() throws Exception {

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/historique/last20")
        )
                .andExpect(status().isMethodNotAllowed());
    }

    // ============================================================
    // 4️⃣ UNKNOWN URL → 404
    // ============================================================

    @Test
    @DisplayName("Unknown URL should return 404")
    void shouldReturn404ForUnknownUrl() throws Exception {

        mockMvc.perform(get("/api/historique/unknown"))
                .andExpect(status().isNotFound());
    }
}
