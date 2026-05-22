// package com.fdjloto.api.controller.admin;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.hamcrest.Matchers.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest
// @ActiveProfiles("test")
// @AutoConfigureMockMvc
// class AdminUserStatsControllerDBTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private JdbcTemplate jdbc;

//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnRealUserStatsFromDatabase() throws Exception {

//         // nettoyage
//         jdbc.execute("DELETE FROM ticket_gains");
//         jdbc.execute("DELETE FROM tickets");
//         jdbc.execute("DELETE FROM users");

//         // user
//         jdbc.update("""
//             INSERT INTO users (id, email, first_name, last_name, is_admin, password)
//             VALUES ('user1', 'user@test.com', 'John', 'Doe', false, 'testpassword')
//         """);

//         // ticket
//         jdbc.update("""
//             INSERT INTO tickets (
//                 id,
//                 user_id,
//                 created_at,
//                 updated_at,
//                 numbers,
//                 lucky_number,
//                 draw_date
//             )
//             VALUES (
//                 't1',
//                 'user1',
//                 NOW(),
//                 NOW(),
//                 '1-2-3-4-5',
//                 7,
//                 '2026-03-23'
//             )
//         """);

//         // gain ✅ CORRIGÉ
//         jdbc.update("""
//             INSERT INTO ticket_gains (
//                 id,
//                 ticket_id,
//                 matching_numbers,
//                 lucky_number_match,
//                 gain_amount
//             )
//             VALUES (
//                 'g1',
//                 't1',
//                 3,
//                 true,
//                 100.50
//             )
//         """);

//         mockMvc.perform(get("/api/admin/users-stats"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(1)))
//                 .andExpect(jsonPath("$[0].id").value("user1"))
//                 .andExpect(jsonPath("$[0].ticketsCount").value(1))
//                 .andExpect(jsonPath("$[0].totalGain").value(100.50));
//     }
// }
