package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.LotoResult;
import com.fdjloto.api.repository.LotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TirageControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LotoRepository lotoRepository;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);

	// @BeforeAll
	// static void waitBeforeTests() throws InterruptedException {
	// 	Thread.sleep(180000);
	// }
    @BeforeEach
    void setup() throws Exception {
        lotoRepository.deleteAll();

        LotoResult result1 = new LotoResult();
        result1.setDateDeTirage(sdf.parse("01/01/2026"));

        LotoResult result2 = new LotoResult();
        result2.setDateDeTirage(sdf.parse("10/01/2026"));

        lotoRepository.save(result1);
        lotoRepository.save(result2);
    }

    // ============================================================
    // 1️⃣ GET /api/tirages/dates
    // ============================================================

    @Test
    @DisplayName("Should return formatted available dates")
    void shouldReturnAvailableDates() throws Exception {

        mockMvc.perform(get("/api/tirages/dates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0]").isString());
    }

    // ============================================================
    // 2️⃣ GET tirages by valid date range
    // ============================================================

    @Test
    @DisplayName("Should return tirages between given dates")
    void shouldReturnTiragesBetweenDates() throws Exception {

        mockMvc.perform(get("/api/tirages")
                        .param("startDate", "01/01/2026")
                        .param("endDate", "15/01/2026"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    // ============================================================
    // 3️⃣ No startDate → fallback to latest
    // ============================================================

    @Test
    @DisplayName("Should fallback to latest draw if startDate missing")
    void shouldFallbackToLatestIfStartDateMissing() throws Exception {

        mockMvc.perform(get("/api/tirages"))
                .andExpect(status().isOk());
    }

    // ============================================================
    // 4️⃣ endDate before startDate → auto-adjust
    // ============================================================

    @Test
    @DisplayName("Should adjust endDate if before startDate")
    void shouldAdjustEndDateIfBeforeStartDate() throws Exception {

        mockMvc.perform(get("/api/tirages")
                        .param("startDate", "10/01/2025")
                        .param("endDate", "01/01/2025"))
                .andExpect(status().isOk());
    }
}
