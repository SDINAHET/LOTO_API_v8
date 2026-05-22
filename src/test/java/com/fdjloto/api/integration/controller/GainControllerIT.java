package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.Ticket;
import com.fdjloto.api.model.TicketGain;
import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.TicketGainRepository;
import com.fdjloto.api.repository.TicketRepository;
import com.fdjloto.api.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.Cookie;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GainControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketGainRepository ticketGainRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String userToken;
    private User user;

    @BeforeEach
    void setup() throws Exception {

        jdbcTemplate.execute("TRUNCATE TABLE ticket_gains CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE tickets CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE users CASCADE");

        user = new User(
                "User",
                "Test",
                "user@test.com",
                passwordEncoder.encode("password"),
                false
        );
        userRepository.save(user);

        userToken = login("user@test.com");
    }

    private String login(String email) throws Exception {
        String response = mockMvc.perform(
                post("/api/auth/login3")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "email": "%s",
                              "password": "password"
                            }
                        """.formatted(email))
        )
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString();

        return response.split("\"token\":\"")[1].split("\"")[0];
    }

    // 🔧 Méthode utilitaire pour créer un ticket VALIDE
    private Ticket createValidTicket() {

        Ticket ticket = new Ticket();
        ticket.setNumbers("1,2,3,4,5");
        ticket.setChanceNumber(7);
        ticket.setDrawDate(LocalDate.now());
        ticket.setDrawDay("Saturday");
        ticket.setUser(user);

        return ticketRepository.save(ticket);
    }

    // ============================================================
    // 1️⃣ CALCULATE
    // ============================================================

    @Test
    @DisplayName("Should calculate gains")
    void shouldCalculateGains() throws Exception {

        mockMvc.perform(get("/api/gains/calculate")
                        .cookie(new Cookie("jwtToken", userToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    // ============================================================
    // 2️⃣ GET ALL
    // ============================================================

    @Test
    @DisplayName("Should return all gains")
    void shouldReturnAllGains() throws Exception {

        Ticket ticket = createValidTicket();

        TicketGain gain = new TicketGain(
                UUID.randomUUID().toString(),
                ticket,
                3,
                false,
                100.0
        );

        ticketGainRepository.save(gain);

        mockMvc.perform(get("/api/gains")
                        .cookie(new Cookie("jwtToken", userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gainAmount").value(100.0));
    }

    // ============================================================
    // 3️⃣ GET BY TICKET ID → 200
    // ============================================================

    @Test
    @DisplayName("Should return gain by ticket ID")
    void shouldReturnGainByTicketId() throws Exception {

        Ticket ticket = createValidTicket();

        TicketGain gain = new TicketGain(
                UUID.randomUUID().toString(),
                ticket,
                4,
                true,
                250.0
        );

        ticketGainRepository.save(gain);

        mockMvc.perform(get("/api/gains/" + ticket.getId())
                        .cookie(new Cookie("jwtToken", userToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gainAmount").value(250.0));
    }

    // ============================================================
    // 4️⃣ GET BY ID → 404
    // ============================================================

    @Test
    @DisplayName("Should return 404 if gain not found")
    void shouldReturn404IfNotFound() throws Exception {

        mockMvc.perform(get("/api/gains/unknown-id")
                        .cookie(new Cookie("jwtToken", userToken)))
                .andExpect(status().isNotFound());
    }
}
