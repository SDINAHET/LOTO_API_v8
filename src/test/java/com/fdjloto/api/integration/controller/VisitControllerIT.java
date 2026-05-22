package com.fdjloto.api.controller;

import com.fdjloto.api.service.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class VisitControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitService visitService;

    @BeforeEach
    void setup() {
        when(visitService.incrementAndGet()).thenReturn(1L);
        when(visitService.get()).thenReturn(1L);
    }

    @Test
    void testAddVisitAndGetTotal() throws Exception {

        mockMvc.perform(get("/api/visits"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));

        verify(visitService, times(1)).incrementAndGet();
    }

    @Test
    void testGetTotalVisits() throws Exception {

        mockMvc.perform(get("/api/visits/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));

        verify(visitService, times(1)).get();
    }
}
