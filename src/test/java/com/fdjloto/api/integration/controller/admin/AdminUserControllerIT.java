// package com.fdjloto.api.controller.admin;

// import com.fdjloto.api.model.User;
// import com.fdjloto.api.repository.UserRepository;
// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.web.servlet.MockMvc;

// import java.util.*;

// import static org.hamcrest.Matchers.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import org.springframework.test.context.ActiveProfiles;

// @SpringBootTest
// @AutoConfigureMockMvc
// @ActiveProfiles("test")
// class AdminUserControllerIT {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private UserRepository userRepository;

//     @MockBean
//     private PasswordEncoder passwordEncoder;

//     @Autowired
//     private ObjectMapper objectMapper;

//     /**
//      * LIST USERS
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldListUsers() throws Exception {

//         User u = new User();
//         u.setId("u1");

//         when(userRepository.findAllByOrderByCreatedAtDesc())
//                 .thenReturn(List.of(u));

//         mockMvc.perform(get("/api/admin/users"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].id").value("u1"));
//     }

//     /**
//      * GET USER OK
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldGetUserById() throws Exception {

//         User u = new User();
//         u.setId("u1");

//         when(userRepository.findById("u1"))
//                 .thenReturn(Optional.of(u));

//         mockMvc.perform(get("/api/admin/users/u1"))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value("u1"));
//     }

//     /**
//      * GET USER 404
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturn404WhenUserNotFound() throws Exception {

//         when(userRepository.findById("bad"))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(get("/api/admin/users/bad"))
//                 .andExpect(status().isNotFound());
//     }

//     /**
//      * CREATE USER
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldCreateUser() throws Exception {

//         User payload = new User();
//         payload.setPassword("123");

//         when(passwordEncoder.encode(any()))
//                 .thenReturn("encoded");

//         when(userRepository.save(any()))
//                 .thenAnswer(inv -> inv.getArgument(0));

//         mockMvc.perform(post("/api/admin/users")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(payload)))
//                 .andExpect(status().isOk());
//     }

//     /**
//      * UPDATE USER
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldUpdateUser() throws Exception {

//         User existing = new User();
//         existing.setId("u1");

//         when(userRepository.findById("u1"))
//                 .thenReturn(Optional.of(existing));

//         when(userRepository.save(any()))
//                 .thenAnswer(inv -> inv.getArgument(0));

//         Map<String,Object> patch = Map.of(
//                 "firstName","John",
//                 "admin",true
//         );

//         mockMvc.perform(put("/api/admin/users/u1")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(patch)))
//                 .andExpect(status().isOk());
//     }

//     /**
//      * UPDATE USER 404
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldReturn404WhenUpdatingMissingUser() throws Exception {

//         when(userRepository.findById("bad"))
//                 .thenReturn(Optional.empty());

//         mockMvc.perform(put("/api/admin/users/bad")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content("{}"))
//                 .andExpect(status().isNotFound());
//     }

//     /**
//      * SOFT DELETE
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldSoftDeleteUser() throws Exception {

//         User u = new User();
//         u.setId("u1");

//         when(userRepository.findById("u1"))
//                 .thenReturn(Optional.of(u));

//         when(passwordEncoder.encode(any()))
//                 .thenReturn("encoded");

//         when(userRepository.save(any()))
//                 .thenReturn(u);

//         mockMvc.perform(delete("/api/admin/users/u1"))
//                 .andExpect(status().isNoContent());
//     }

//     /**
//      * RESET PASSWORD (admin fournit)
//      */
//     @Test
//     @WithMockUser(roles = "ADMIN")
//     void shouldResetPasswordProvided() throws Exception {

//         User u = new User();
//         u.setId("u1");

//         when(userRepository.findById("u1"))
//                 .thenReturn(Optional.of(u));

//         when(passwordEncoder.encode(any()))
//                 .thenReturn("encoded");

//         when(userRepository.save(any()))
//                 .thenReturn(u);

//         Map<String,Object> body = Map.of("newPassword","123");

//         mockMvc.perform(post("/api/admin/users/u1/reset-password")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(body)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.message").exists());
//     }

//     /**
//      * RESET PASSWORD généré
//      */
//     // @Test
//     // @WithMockUser(roles = "ADMIN")
//     // void shouldGenerateTemporaryPassword() throws Exception {

//     //     User u = new User();
//     //     u.setId("u1");

//     //     when(userRepository.findById("u1"))
//     //             .thenReturn(Optional.of(u));

//     //     when(passwordEncoder.encode(any()))
//     //             .thenReturn("encoded");

//     //     when(userRepository.save(any()))
//     //             .thenReturn(u);

//     //     mockMvc.perform(post("/api/admin/users/u1/reset-password"))
//     //             .andExpect(status().isOk())
//     //             .andExpect(jsonPath("$.temporaryPassword").exists());
//     // }
// }


package com.fdjloto.api.integration;

import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 🧪 AdminUserController IT
 * ✔ Couverture complète
 * ✔ Sécurité ADMIN simulée
 * ✔ Style identique AdminTicketControllerIT
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "admin", roles = {"ADMIN"}) // ✅ IMPORTANT
class AdminUserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    // =========================
    // Helper
    // =========================

    private User createUser(String id) {
        User u = new User();
        u.setId(id);
        u.setEmail("test@test.com");
        return u;
    }

    // =========================
    // GET ALL
    // =========================

    @Test
    @DisplayName("GET /users → OK (list)")
    void shouldReturnAllUsers() throws Exception {

        User u = createUser("u1");

        when(userRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of(u));

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value("u1"));
    }

    @Test
    @DisplayName("GET /users → empty list")
    void shouldReturnEmptyList() throws Exception {

        when(userRepository.findAllByOrderByCreatedAtDesc())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // =========================
    // GET BY ID
    // =========================

    @Test
    @DisplayName("GET /users/{id} → OK")
    void shouldReturnUserById() throws Exception {

        User u = createUser("u1");

        when(userRepository.findById("u1"))
                .thenReturn(Optional.of(u));

        mockMvc.perform(get("/api/admin/users/u1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("u1"));
    }

    @Test
    @DisplayName("GET /users/{id} → 404")
    void shouldReturn404WhenUserNotFound() throws Exception {

        when(userRepository.findById("bad"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/admin/users/bad"))
                .andExpect(status().isNotFound());
    }

    // =========================
    // CREATE
    // =========================

    // @Test
    // @DisplayName("POST /users → OK")
    // void shouldCreateUser() throws Exception {

    //     User payload = new User();
    //     payload.setPassword("123");

    //     when(passwordEncoder.encode(any()))
    //             .thenReturn("encoded");

    //     when(userRepository.save(any()))
    //             .thenAnswer(inv -> inv.getArgument(0));

    //     mockMvc.perform(post("/api/admin/users")
    //                     .contentType(MediaType.APPLICATION_JSON)
    //                     .content(objectMapper.writeValueAsString(payload)))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.password").value("encoded"));
    // }

	// @Test
	// @DisplayName("POST /users → OK")
	// void shouldCreateUser() throws Exception {

	// 	User payload = new User();
	// 	payload.setEmail("test@test.com"); // ✅ OBLIGATOIRE
	// 	payload.setPassword("123");

	// 	when(passwordEncoder.encode(any()))
	// 			.thenReturn("encoded");

	// 	when(userRepository.save(any()))
	// 			.thenAnswer(inv -> inv.getArgument(0));

	// 	mockMvc.perform(post("/api/admin/users")
	// 					.contentType(MediaType.APPLICATION_JSON)
	// 					.content(objectMapper.writeValueAsString(payload)))
	// 			.andExpect(status().isOk())
	// 			.andExpect(jsonPath("$.email").value("test@test.com"))
	// 			.andExpect(jsonPath("$.password").value("encoded"));
	// }

	@Test
	@DisplayName("POST /users → OK")
	void shouldCreateUser() throws Exception {

		User payload = new User();
		payload.setEmail("test@test.com");
		payload.setPassword("123456");

		// 🔥 AJOUT OBLIGATOIRE
		payload.setFirstName("John");
		payload.setLastName("Doe");

		when(passwordEncoder.encode(any()))
				.thenReturn("encoded");

		when(userRepository.save(any()))
				.thenAnswer(inv -> inv.getArgument(0));

		mockMvc.perform(post("/api/admin/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(payload)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("test@test.com"))
				.andExpect(jsonPath("$.password").value("encoded"));
	}


    // =========================
    // UPDATE
    // =========================

    @Test
    @DisplayName("PUT /users → OK")
    void shouldUpdateUser() throws Exception {

        User u = createUser("u1");

        when(userRepository.findById("u1"))
                .thenReturn(Optional.of(u));

        when(userRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Map<String,Object> patch = Map.of(
                "firstName","John",
                "admin",true
        );

        mockMvc.perform(put("/api/admin/users/u1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.admin").value(true));
    }


    @Test
    @DisplayName("PUT /users → 404")
    void shouldReturn404WhenUpdatingMissingUser() throws Exception {

        when(userRepository.findById("bad"))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/admin/users/bad")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    // =========================
    // DELETE
    // =========================

    @Test
    @DisplayName("DELETE /users → OK")
    void shouldSoftDeleteUser() throws Exception {

        User u = createUser("u1");

        when(userRepository.findById("u1"))
                .thenReturn(Optional.of(u));

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        when(userRepository.save(any()))
                .thenReturn(u);

        mockMvc.perform(delete("/api/admin/users/u1"))
                .andExpect(status().isNoContent());
    }

    // =========================
    // RESET PASSWORD
    // =========================

    @Test
    @DisplayName("POST /users/reset-password → OK (provided)")
    void shouldResetPasswordProvided() throws Exception {

        User u = createUser("u1");

        when(userRepository.findById("u1"))
                .thenReturn(Optional.of(u));

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        when(userRepository.save(any()))
                .thenReturn(u);

        Map<String,Object> body = Map.of("newPassword","123");

        mockMvc.perform(post("/api/admin/users/u1/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /users/reset-password → OK (generated)")
    void shouldGenerateTemporaryPassword() throws Exception {

        User u = createUser("u1");

        when(userRepository.findById("u1"))
                .thenReturn(Optional.of(u));

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        when(userRepository.save(any()))
                .thenReturn(u);

        mockMvc.perform(post("/api/admin/users/u1/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)) // ✅ IMPORTANT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.temporaryPassword").exists())
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
        @DisplayName("DELETE /users → 404")
        void shouldReturn404WhenDeletingMissingUser() throws Exception {

        when(userRepository.findById("bad"))
                .thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/admin/users/bad"))
                .andExpect(status().isNotFound());
        }




        @Test
        @DisplayName("POST /users/reset-password → 404")
        void shouldReturn404WhenResettingMissingUser() throws Exception {

        when(userRepository.findById("bad"))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/admin/users/bad/reset-password"))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("PUT /users → update password")
        void shouldUpdatePassword() throws Exception {

        User u = createUser("u1");

        when(userRepository.findById("u1"))
                .thenReturn(Optional.of(u));

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        when(userRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        Map<String,Object> patch = Map.of(
                "password","123"
        );

        mockMvc.perform(put("/api/admin/users/u1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").value("encoded"));
        }
}
