package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.Historique20Result;
import com.fdjloto.api.repository.Historique20Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DernierTiragePageControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Historique20Repository historique20Repository;

    @BeforeEach
    void cleanDb() {
        historique20Repository.deleteAll();
    }

    // ============================================================
    // ✅ 1️⃣ Should return page when result exists
    // ============================================================

    @Test
    @DisplayName("Should return dernier-tirage page with model")
    void shouldReturnDernierTiragePage() throws Exception {

        Historique20Result result = new Historique20Result();
        result.setDateDeTirage(Date.from(Instant.now()));
        result.setBoule1(1);
        result.setBoule2(2);
        result.setBoule3(3);
        result.setBoule4(4);
        result.setBoule5(5);
        result.setNumeroChance(7);

        historique20Repository.save(result);

        mockMvc.perform(get("/dernier-tirage"))
                .andExpect(status().isOk())
                .andExpect(view().name("dernier-tirage"))
                .andExpect(model().attributeExists("tirage"))
                .andExpect(model().attributeExists("dateFr"))
                .andExpect(model().attributeExists("nums"))
                .andExpect(model().attributeExists("seoTitle"))
                .andExpect(model().attributeExists("seoDescription"));
    }

    // ============================================================
    // ❌ 2️⃣ Should return 404 when no result
    // ============================================================

    @Test
    @DisplayName("Should return 404 if no tirage")
    void shouldReturn404IfNoResult() throws Exception {

        mockMvc.perform(get("/dernier-tirage"))
                .andExpect(status().isNotFound());
    }

    // ============================================================
    // ❌ 3️⃣ Should return 500 if date is null
    // ============================================================

    @Test
    @DisplayName("Should return 500 if dateDeTirage is null")
    void shouldReturn500IfDateMissing() throws Exception {

        Historique20Result result = new Historique20Result();
        result.setDateDeTirage(null);
        result.setBoule1(1);
        result.setBoule2(2);
        result.setBoule3(3);
        result.setBoule4(4);
        result.setBoule5(5);
        result.setNumeroChance(7);

        historique20Repository.save(result);

        mockMvc.perform(get("/dernier-tirage"))
                .andExpect(status().isInternalServerError());
    }
}
