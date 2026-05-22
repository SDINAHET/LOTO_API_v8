// package com.fdjloto.api.controller.admin;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;

// import com.fdjloto.api.controller.admin.AdminUserStatsController;

// import java.math.BigDecimal;
// import java.time.Instant;
// import java.util.List;

// import static org.hamcrest.Matchers.*;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import org.springframework.jdbc.core.RowMapper;

// import org.springframework.test.context.ActiveProfiles;

// @SpringBootTest
// @ActiveProfiles("test")
// @AutoConfigureMockMvc
// class AdminUserStatsControllerIT {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private JdbcTemplate jdbc;

//     /**
//      * Test retour stats utilisateurs
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnUserStats() throws Exception {

//         AdminUserStatsController.UserStatsView view =
//                 new AdminUserStatsController.UserStatsView(
//                         "user1",
//                         "user@test.com",
//                         "John",
//                         "Doe",
//                         false,
//                         5,
//                         new BigDecimal("100.50"),
//                         new BigDecimal("50.00"),
//                         Instant.now()
//                 );

// 		when(jdbc.query(
// 				org.mockito.ArgumentMatchers.anyString(),
// 				org.mockito.ArgumentMatchers.<RowMapper<AdminUserStatsController.UserStatsView>>any()
// 		)).thenReturn(List.of(view));

//         mockMvc.perform(get("/api/admin/users-stats"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(1)))
//                 .andExpect(jsonPath("$[0].id").value("user1"))
//                 .andExpect(jsonPath("$[0].email").value("user@test.com"))
//                 .andExpect(jsonPath("$[0].firstName").value("John"))
//                 .andExpect(jsonPath("$[0].lastName").value("Doe"))
//                 .andExpect(jsonPath("$[0].ticketsCount").value(5))
//                 .andExpect(jsonPath("$[0].totalGain").value(100.50));
//     }

//     /**
//      * Test aucun utilisateur
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturnEmptyList() throws Exception {

// 		when(jdbc.query(
// 				org.mockito.ArgumentMatchers.anyString(),
// 				org.mockito.ArgumentMatchers.<RowMapper<AdminUserStatsController.UserStatsView>>any()
// 		)).thenReturn(List.of());

//         mockMvc.perform(get("/api/admin/users-stats"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(0)));
//     }

//     /**
//      * Sécurité : accès interdit sans ADMIN
//      */
//     @Test
//     @WithMockUser(roles = "USER")
//     void shouldReturnForbiddenForNonAdmin() throws Exception {

//         mockMvc.perform(get("/api/admin/users-stats"))
//                 .andExpect(status().isForbidden());
//     }
// }

package com.fdjloto.api.controller.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AdminUserStatsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JdbcTemplate jdbc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnUserStats() throws Exception {

        AdminUserStatsController.UserStatsView view =
                new AdminUserStatsController.UserStatsView(
                        "user1",
                        "user@test.com",
                        "John",
                        "Doe",
                        false,
                        5,
                        new BigDecimal("100.50"),
                        new BigDecimal("50.00"),
                        Instant.now()
                );

        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of(view));

        mockMvc.perform(get("/api/admin/users-stats"))
                .andExpect(status().isOk())

                // taille
                .andExpect(jsonPath("$", hasSize(1)))

                // champs simples
                .andExpect(jsonPath("$[0].id").value("user1"))
                .andExpect(jsonPath("$[0].email").value("user@test.com"))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))

                // boolean
                .andExpect(jsonPath("$[0].is_admin").value(false))

                // int
                .andExpect(jsonPath("$[0].ticketsCount").value(5))

                // BigDecimal (safe)
                .andExpect(jsonPath("$[0].totalGain").value(100.50))
                .andExpect(jsonPath("$[0].bestGain").value(50.00));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnEmptyList() throws Exception {

        when(jdbc.query(anyString(), any(RowMapper.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/admin/users-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenForNonAdmin() throws Exception {

        mockMvc.perform(get("/api/admin/users-stats"))
                .andExpect(status().isForbidden());
    }
	
}
