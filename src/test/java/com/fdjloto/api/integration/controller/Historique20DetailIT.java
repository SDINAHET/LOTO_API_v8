package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.Historique20Detail;
import com.fdjloto.api.service.Historique20DetailService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class Historique20DetailControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Historique20DetailService lotoService;

    // ============================================================
    // 1️⃣ GET TIRAGE BY DATE → 200 OK
    // ============================================================

    @Test
    @DisplayName("Should return draw details by date")
    void shouldReturnTirageByDate() throws Exception {

        Historique20Detail detail = new Historique20Detail();

        Date date = new SimpleDateFormat("dd/MM/yyyy")
                .parse("15/03/2025");

        detail.setDateDeTirage(date);
        detail.setBoule1(1);
        detail.setBoule2(2);
        detail.setBoule3(3);
        detail.setBoule4(4);
        detail.setBoule5(5);
        detail.setNumeroChance(6);

        when(lotoService.getTirageByDate("2025-03-15"))
                .thenReturn(Optional.of(detail));

        mockMvc.perform(
                get("/api/historique/last20/Detail/tirage/2025-03-15")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.numeroChance").value(6));
    }

    // ============================================================
    // 2️⃣ GET TIRAGE BY DATE → 404
    // ============================================================

    @Test
    @DisplayName("Should return 404 if draw not found")
    void shouldReturn404IfTirageNotFound() throws Exception {

        when(lotoService.getTirageByDate("2025-01-01"))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                get("/api/historique/last20/Detail/tirage/2025-01-01")
        )
        .andExpect(status().isNotFound());
    }

    // ============================================================
    // 3️⃣ GET TIRAGES RANGE → 200 OK
    // ============================================================

    @Test
    @DisplayName("Should return draws within date range")
    void shouldReturnTiragesInRange() throws Exception {

        Historique20Detail detail = new Historique20Detail();

        Date date = new SimpleDateFormat("dd/MM/yyyy")
                .parse("15/03/2025");

        detail.setDateDeTirage(date);
        detail.setNumeroChance(7);

        when(lotoService.getTiragesParPlageDeDates("2025-03-01", "2025-03-31"))
                .thenReturn(List.of(detail));

        mockMvc.perform(
                get("/api/historique/last20/Detail/tirages")
                        .param("startDate", "2025-03-01")
                        .param("endDate", "2025-03-31")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].numeroChance").value(7));
    }

    // ============================================================
    // 4️⃣ GET SINGLE DATE (no endDate) → 200 OK
    // ============================================================

    @Test
    @DisplayName("Should return single day search if endDate missing")
    void shouldHandleSingleDateSearch() throws Exception {

        Historique20Detail detail = new Historique20Detail();

        Date date = new SimpleDateFormat("dd/MM/yyyy")
                .parse("15/03/2025");

        detail.setDateDeTirage(date);
        detail.setNumeroChance(9);

        when(lotoService.getTiragesParPlageDeDates("2025-03-15", "2025-03-15"))
                .thenReturn(List.of(detail));

        mockMvc.perform(
                get("/api/historique/last20/Detail/tirages")
                        .param("startDate", "2025-03-15")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].numeroChance").value(9));
    }

    // ============================================================
    // 5️⃣ RANGE → 404
    // ============================================================

    @Test
    @DisplayName("Should return 404 if no draws found in range")
    void shouldReturn404IfRangeEmpty() throws Exception {

        when(lotoService.getTiragesParPlageDeDates("2025-01-01", "2025-01-31"))
                .thenReturn(List.of());

        mockMvc.perform(
                get("/api/historique/last20/Detail/tirages")
                        .param("startDate", "2025-01-01")
                        .param("endDate", "2025-01-31")
        )
        .andExpect(status().isNotFound());
    }

    // ============================================================
    // 6️⃣ WRONG METHOD → 405
    // ============================================================

    @Test
    @DisplayName("POST should return 405")
    void shouldReturn405ForWrongMethod() throws Exception {

        mockMvc.perform(
                post("/api/historique/last20/Detail/tirage/2025-03-15")
        )
        .andExpect(status().isMethodNotAllowed());
    }

    // ============================================================
    // 7️⃣ UNKNOWN URL → 404
    // ============================================================

    @Test
    @DisplayName("Unknown URL should return 404")
    void shouldReturn404ForUnknownUrl() throws Exception {

        mockMvc.perform(
                get("/api/historique/last20/Detail/unknown")
        )
        .andExpect(status().isNotFound());
    }
}
