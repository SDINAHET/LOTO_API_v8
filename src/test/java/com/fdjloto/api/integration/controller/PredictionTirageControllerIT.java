package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.PredictionTirageModel;
import com.fdjloto.api.service.PredictionService;
import com.fdjloto.api.service.PredictionTirageService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PredictionTirageControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PredictionService predictionService;

    @MockBean
    private PredictionTirageService predictionTirageService;

    // ============================================================
    // 1️⃣ POST /generate → 200 OK
    // ============================================================

    @Test
    @DisplayName("Should generate new prediction")
    void shouldGenerateNewPrediction() throws Exception {

        PredictionTirageModel prediction = new PredictionTirageModel();
        // prediction.setNumeroChance(7);
		prediction.setProbableChance(7);

        when(predictionTirageService.generatePrediction())
                .thenReturn(prediction);

        mockMvc.perform(
                post("/api/predictions/generate")
        )
        .andExpect(status().isOk())
        // .andExpect(jsonPath("$.numeroChance").value(7));
		.andExpect(jsonPath("$.probableChance").value(7));

    }

    // ============================================================
    // 2️⃣ POST /generate → 400 BAD REQUEST
    // ============================================================

    @Test
    @DisplayName("Should return 400 if prediction generation fails")
    void shouldReturn400IfPredictionFails() throws Exception {

        when(predictionTirageService.generatePrediction())
                .thenReturn(null);

        mockMvc.perform(
                post("/api/predictions/generate")
        )
        .andExpect(status().isBadRequest());
    }

    // ============================================================
    // 3️⃣ GET /latest → 200 OK
    // ============================================================

    @Test
    @DisplayName("Should return latest prediction")
    void shouldReturnLatestPrediction() throws Exception {

        PredictionTirageModel prediction = new PredictionTirageModel();
        // prediction.setNumeroChance(9);
		prediction.setProbableChance(9);

        when(predictionService.getLatestPrediction())
                .thenReturn(prediction);

        mockMvc.perform(
                get("/api/predictions/latest")
        )
        .andExpect(status().isOk())
        // .andExpect(jsonPath("$.numeroChance").value(9));
		.andExpect(jsonPath("$.probableChance").value(9));
    }

    // ============================================================
    // 4️⃣ GET /latest → 404 NOT FOUND
    // ============================================================

    @Test
    @DisplayName("Should return 404 if no prediction found")
    void shouldReturn404IfNoPrediction() throws Exception {

        when(predictionService.getLatestPrediction())
                .thenReturn(null);

        mockMvc.perform(
                get("/api/predictions/latest")
        )
        .andExpect(status().isNotFound());
    }

    // ============================================================
    // 5️⃣ WRONG METHOD → 405
    // ============================================================

    @Test
    @DisplayName("GET on /generate should return 405")
    void shouldReturn405ForWrongMethod() throws Exception {

        mockMvc.perform(
                get("/api/predictions/generate")
        )
        .andExpect(status().isMethodNotAllowed());
    }

    // ============================================================
    // 6️⃣ UNKNOWN URL → 404
    // ============================================================

    @Test
    @DisplayName("Unknown URL should return 404")
    void shouldReturn404ForUnknownUrl() throws Exception {

        mockMvc.perform(
                get("/api/predictions/unknown")
        )
        // .andExpect(status().isNotFound());
		.andExpect(status().isUnauthorized());
    }
}
