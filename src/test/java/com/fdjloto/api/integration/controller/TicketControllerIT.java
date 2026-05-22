package com.fdjloto.api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdjloto.api.dto.TicketDTO;
import com.fdjloto.api.model.Ticket;
import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.TicketRepository;
import com.fdjloto.api.repository.UserRepository;
import com.fdjloto.api.security.JwtUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.Cookie;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for TicketController.
 *
 * This class tests the real API endpoints using MockMvc and the real Spring context.
 */
@SpringBootTest
// @AutoConfigureMockMvc
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TicketControllerIT {

    /**
     * MockMvc allows performing HTTP requests without starting a real server.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Repository used to manipulate users in the test database.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Repository used to manipulate tickets in the test database.
     */
    @Autowired
    private TicketRepository ticketRepository;

    /**
     * ObjectMapper used to convert Java objects into JSON.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * JwtUtils is mocked because we do not want to generate real JWT tokens in tests.
     */
    @MockBean
    private JwtUtils jwtUtils;

    private User user;
    private User admin;
    private Ticket ticket;

    /**
     * Setup executed before each test.
     * It prepares test data and mocks authentication.
     */
    @BeforeEach
    void setup() {

        // Clean database before each test
        ticketRepository.deleteAll();
        userRepository.deleteAll();

        /**
         * Create a normal user
         */
        user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail("user@test.com");
        user.setPassword("password");

        // REQUIRED fields due to validation annotations
        user.setFirstName("Test");
        user.setLastName("User");

        user.setAdmin(false);

        userRepository.save(user);

        /**
         * Create an admin user
         */
        admin = new User();
        admin.setId(UUID.randomUUID().toString());
        admin.setEmail("admin@test.com");
        admin.setPassword("password");

        // REQUIRED fields
        admin.setFirstName("Admin");
        admin.setLastName("User");

        admin.setAdmin(true);

        userRepository.save(admin);

        /**
         * Create a ticket belonging to the normal user
         */
        ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setUser(user);
        ticket.setNumbers("1-2-3-4-5");
        ticket.setChanceNumber(7);
        ticket.setDrawDate(LocalDate.now());

        ticketRepository.save(ticket);

        /**
         * Mock JWT authentication
         * The token "valid-user" will return the user's email.
         */
        when(jwtUtils.getUserFromJwtToken("valid-user"))
                .thenReturn(user.getEmail());

        /**
         * Token for admin
         */
        when(jwtUtils.getUserFromJwtToken("valid-admin"))
                .thenReturn(admin.getEmail());
    }

    /**
     * Test: a normal user should only retrieve their own tickets.
     */
    @Test
    @DisplayName("User should retrieve only their tickets")
    void userShouldGetOwnTickets() throws Exception {

        mockMvc.perform(get("/api/tickets")
                        .cookie(new Cookie("jwtToken", "valid-user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value(ticket.getId().toString()));
    }

    /**
     * Test: an admin can retrieve all tickets.
     */
    @Test
    @DisplayName("Admin should retrieve all tickets")
    void adminShouldGetAllTickets() throws Exception {

        mockMvc.perform(get("/api/tickets")
                        .cookie(new Cookie("jwtToken", "valid-admin")))
                .andExpect(status().isOk());
    }

    /**
     * Test: a user can delete their own ticket.
     */
    @Test
    @DisplayName("User should delete their own ticket")
    void userShouldDeleteOwnTicket() throws Exception {

        mockMvc.perform(delete("/api/tickets/" + ticket.getId())
                        .cookie(new Cookie("jwtToken", "valid-user")))
                .andExpect(status().isNoContent());
    }

    /**
     * Test: a user can update their ticket.
     */
    @Test
    @DisplayName("User should update their ticket")
    void userShouldUpdateTicket() throws Exception {

        TicketDTO dto = new TicketDTO();
        dto.setNumbers("10-20-30-40-50");
        dto.setChanceNumber("9");
        dto.setDrawDate(LocalDate.now().toString());

        mockMvc.perform(put("/api/tickets/" + ticket.getId())
                        .cookie(new Cookie("jwtToken", "valid-user"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chanceNumber").value("9"));
    }

    /**
     * Test: a user can create a new ticket.
     */
    @Test
    @DisplayName("User should create ticket")
    void userShouldCreateTicket() throws Exception {

        TicketDTO dto = new TicketDTO();
        dto.setNumbers("10-20-30-40-50");
        dto.setChanceNumber("5");
        dto.setDrawDate(LocalDate.now().toString());

        mockMvc.perform(post("/api/tickets")
                        .cookie(new Cookie("jwtToken", "valid-user"))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
