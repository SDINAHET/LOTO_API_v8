// package com.fdjloto.api.e2e;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.HashMap;
// import java.util.Map;
// import java.util.UUID;

// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import org.springframework.test.web.servlet.MvcResult;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import jakarta.servlet.http.Cookie;

// @SpringBootTest
// @AutoConfigureMockMvc
// public class LotoTrackerE2ETest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private ObjectMapper objectMapper;

//     @Test
//     void testFullUserFlow() throws Exception {

//         // -------------------------------
//         // 1. REGISTER USER
//         // -------------------------------
//         String unique = UUID.randomUUID().toString().substring(0, 8);

//         Map<String, Object> register = new HashMap<>();
//         register.put("email", "stephane.dinahet." + unique + "@gmail.com");
//         register.put("password", "StrongPassword123!");
//         register.put("firstName", "Stéphane");
//         register.put("lastName", "Dinahet");
//         register.put("role", "USER");

//         mockMvc.perform(post("/api/auth/register")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(register)))
//                 .andExpect(status().isOk());

//         // -------------------------------
//         // 2. LOGIN
//         // -------------------------------
//         Map<String, Object> login = new HashMap<>();
//         login.put("email", register.get("email"));
//         login.put("password", register.get("password"));

//         MvcResult loginResult = mockMvc.perform(post("/api/auth/login3")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(login)))
//                 .andExpect(status().isOk())
//                 .andReturn();

//         String token = objectMapper
//                 .readTree(loginResult.getResponse().getContentAsString())
//                 .get("token").asText();

//         Cookie jwtCookie = new Cookie("jwtToken", token);

//         // -------------------------------
//         // 3. CREATE TICKET
//         // -------------------------------
//         Map<String, Object> ticket = new HashMap<>();
//         ticket.put("numbers", "3-11-19-27-42");
//         ticket.put("chanceNumber", "9");
//         ticket.put("drawDate", "2025-06-15");
//         // ticket.put("drawDay", "lundi");

//         MvcResult ticketResult = mockMvc.perform(post("/api/tickets")
//                         .with(csrf())
//                         .cookie(jwtCookie)
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(ticket)))
//                         .andExpect(status().isOk())
//                         .andReturn();

//         String response = ticketResult.getResponse().getContentAsString();
//         String ticketId = objectMapper.readTree(response).get("id").asText();

//         // -------------------------------
//         // 4. VERIFY TICKET
//         // -------------------------------
//         mockMvc.perform(get("/api/tickets")
//                         .cookie(jwtCookie))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string(org.hamcrest.Matchers.containsString(ticketId)));

//         // -------------------------------
//         // 5. GET HISTORIQUE
//         // -------------------------------
//         mockMvc.perform(get("/api/historique/last20")
//                         .cookie(jwtCookie))
//                 .andExpect(status().isOk());

//         // -------------------------------
//         // 6. DELETE TICKET
//         // -------------------------------
//         mockMvc.perform(delete("/api/tickets/" + ticketId)
//                         .with(csrf())
//                         .cookie(jwtCookie))
//                 .andExpect(status().isNoContent());

//         // -------------------------------
//         // 7. VERIFY DELETE
//         // -------------------------------
//         mockMvc.perform(get("/api/tickets/" + ticketId)
//                         .cookie(jwtCookie))
//                 .andExpect(status().isNotFound());
//     }
// }

package com.fdjloto.api.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import jakarta.servlet.http.Cookie;

import org.testcontainers.junit.jupiter.Testcontainers;

import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
// @ActiveProfiles("ci")
@ActiveProfiles("test")
public class LotoTrackerE2ETest {

    @Container
    static MongoDBContainer mongo =
        new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void setMongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongo::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFullUserFlow() throws Exception {

        String unique = UUID.randomUUID().toString().substring(0, 8);

        // -------------------------------
        // REGISTER
        // -------------------------------
        Map<String, Object> register = new HashMap<>();
        register.put("email", "stephane.dinahet." + unique + "@gmail.com");
        register.put("password", "StrongPassword123!");
        register.put("firstName", "Stéphane");
        register.put("lastName", "Dinahet");
        register.put("role", "USER");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        // -------------------------------
        // LOGIN
        // -------------------------------
        Map<String, Object> login = new HashMap<>();
        login.put("email", register.get("email"));
        login.put("password", register.get("password"));

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper
                .readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();

        Cookie jwtCookie = new Cookie("jwtToken", token);

        // -------------------------------
        // CREATE TICKET
        // -------------------------------
        Map<String, Object> ticket = new HashMap<>();
        ticket.put("numbers", "3-11-19-27-42");
        ticket.put("chanceNumber", "9");
        ticket.put("drawDate", "2025-06-15");

        MvcResult ticketResult = mockMvc.perform(post("/api/tickets")
                        .with(csrf())
                        .cookie(jwtCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticket)))
                .andExpect(status().isOk())
                .andReturn();

        String ticketId = objectMapper
                .readTree(ticketResult.getResponse().getContentAsString())
                .get("id").asText();

        // -------------------------------
        // VERIFY
        // -------------------------------
        mockMvc.perform(get("/api/tickets")
                        .cookie(jwtCookie))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString(ticketId)));

        // -------------------------------
        // HISTORIQUE
        // -------------------------------
        mockMvc.perform(get("/api/historique/last20")
                        .cookie(jwtCookie))
                .andExpect(status().isOk());

        // -------------------------------
        // DELETE
        // -------------------------------
        mockMvc.perform(delete("/api/tickets/" + ticketId)
                        .with(csrf())
                        .cookie(jwtCookie))
                .andExpect(status().isNoContent());

        // -------------------------------
        // VERIFY DELETE
        // -------------------------------
        mockMvc.perform(get("/api/tickets/" + ticketId)
                        .cookie(jwtCookie))
                .andExpect(status().isNotFound());
    }
}
