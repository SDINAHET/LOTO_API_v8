package com.fdjloto.api.integration;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicketCrudIT {

    @Autowired
    private MockMvc mockMvc;

    private static Cookie jwtCookie;
    private static String ticketId;

    private static final String EMAIL =
            "cruduser" + System.currentTimeMillis() + "@loto.com";

    private static final String PASSWORD = "password123";

    // ------------------------------------------------
    // REGISTER
    // ------------------------------------------------

    @Test
    @Order(1)
    void register_user() throws Exception {

        String body = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Crud",
          "lastName":"User"
        }
        """.formatted(EMAIL, PASSWORD);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());
    }

    // ------------------------------------------------
    // LOGIN
    // ------------------------------------------------

    @Test
    @Order(2)
    void login() throws Exception {

        String body = """
        {
          "email":"%s",
          "password":"%s"
        }
        """.formatted(EMAIL, PASSWORD);

        MvcResult result = mockMvc.perform(post("/api/auth/login3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("jwtToken"))
                .andReturn();

        jwtCookie = result.getResponse().getCookie("jwtToken");
    }

    // ------------------------------------------------
    // CREATE TICKET
    // ------------------------------------------------

    @Test
    @Order(3)
    void create_ticket() throws Exception {

        String ticket = """
        {
          "numbers":"1,2,3,4,5",
          "chanceNumber":"7",
          "drawDate":"2024-06-10"
        }
        """;

        MvcResult result = mockMvc.perform(post("/api/tickets")
                .cookie(jwtCookie)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ticket))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numbers").value("1,2,3,4,5"))
                .andReturn();

        String response = result.getResponse().getContentAsString();

        ticketId = response.split("\"id\":\"")[1].split("\"")[0];
    }

    // ------------------------------------------------
    // GET ALL TICKETS
    // ------------------------------------------------

    @Test
    @Order(4)
    void get_all_tickets() throws Exception {

        mockMvc.perform(get("/api/tickets")
                .cookie(jwtCookie))
                .andExpect(status().isOk());
    }

    // ------------------------------------------------
    // GET TICKET BY ID
    // ------------------------------------------------

    @Test
    @Order(5)
    void get_ticket_by_id() throws Exception {

        mockMvc.perform(get("/api/tickets/" + ticketId)
                .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId));
    }

    // ------------------------------------------------
    // UPDATE TICKET
    // ------------------------------------------------

    @Test
    @Order(6)
    void update_ticket() throws Exception {

        String update = """
        {
          "numbers":"10,11,12,13,14",
          "chanceNumber":"5",
          "drawDate":"2024-06-10"
        }
        """;

        mockMvc.perform(put("/api/tickets/" + ticketId)
                .cookie(jwtCookie)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(update))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numbers").value("10,11,12,13,14"));
    }

    // ------------------------------------------------
    // DELETE TICKET
    // ------------------------------------------------

    @Test
    @Order(7)
    void delete_ticket() throws Exception {

        mockMvc.perform(delete("/api/tickets/" + ticketId)
                .cookie(jwtCookie)
                .with(csrf()))
                .andExpect(status().isNoContent());
    }

}
