package com.fdjloto.api.integration.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SitemapControllerIT {

    @Autowired
    private MockMvc mockMvc;

    // ============================================================
    // 1️⃣ sitemap.xml (index)
    // ============================================================

    @Test
    @DisplayName("Should return sitemap index XML")
    void shouldReturnSitemapIndex() throws Exception {

        mockMvc.perform(get("/sitemap.xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/xml"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<sitemapindex")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("sitemap-pages.xml")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("sitemap-tirages.xml")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("sitemap-static.xml")));
    }

    // ============================================================
    // 2️⃣ sitemap-pages.xml
    // ============================================================

    @Test
    @DisplayName("Should return sitemap pages XML")
    void shouldReturnSitemapPages() throws Exception {

        mockMvc.perform(get("/sitemap-pages.xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/xml"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<urlset")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/dernier-tirage")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/resultat-loto-aujourdhui")));
    }

    // ============================================================
    // 3️⃣ sitemap-static.xml
    // ============================================================

    @Test
    @DisplayName("Should return sitemap static XML")
    void shouldReturnSitemapStatic() throws Exception {

        mockMvc.perform(get("/sitemap-static.xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/xml"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("politique_confidentialite.html")));
    }

    // ============================================================
    // 4️⃣ sitemap-tirages.xml
    // ============================================================

    @Test
    @DisplayName("Should return sitemap tirages XML")
    void shouldReturnSitemapTirages() throws Exception {

        mockMvc.perform(get("/sitemap-tirages.xml"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/xml"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<urlset")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/tirage/")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("/resultat-loto-")));
    }
}
