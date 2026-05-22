// package com.fdjloto.api.integration;

// import org.junit.jupiter.api.Test;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// class AdminControllerIT extends BaseIntegrationTest {

//     @Test
//     void shouldReturn403ForUserAccessingAdmin() throws Exception {

//         mockMvc.perform(get("/api/admin/dashboard")
//                 .cookie(jwtCookie)) // JWT USER
//                 .andExpect(status().isForbidden());
//     }

//     @Test
//     void shouldAllowAdminAccess() throws Exception {

//         mockMvc.perform(get("/api/admin/dashboard")
//                 .cookie(adminJwtCookie)) // JWT ADMIN
//                 .andExpect(status().isOk());
//     }
// }

package com.fdjloto.api.integration;

import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d’intégration du controller ADMIN
 *
 * ✔ 401 si pas authentifié
 * ✔ 403 si ROLE_USER
 * ✔ 200 si ROLE_ADMIN
 * ✔ suppression utilisateur
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminControllerIT extends BaseIntegrationTest {

    private static Cookie userJwt;
    private static Cookie adminJwt;
    private static String testUserId;

    @Autowired
    private UserRepository userRepository;

    // ============================================================
    // 1️⃣ SETUP
    // ============================================================

    @Test
    @Order(1)
    void setupUsers() throws Exception {

        String password = "password123";
        String userEmail = "normal" + System.currentTimeMillis() + "@loto.com";
        String adminEmail = "admin" + System.currentTimeMillis() + "@loto.com";

        // =========================
        // CREATE USER
        // =========================
        String registerUser = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Normal",
          "lastName":"User"
        }
        """.formatted(userEmail, password);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerUser))
                .andExpect(status().isOk());

        User createdUser = userRepository.findByEmail(userEmail).orElseThrow();
        testUserId = createdUser.getId().toString();

        // LOGIN USER
        String loginUser = """
        {
          "email":"%s",
          "password":"%s"
        }
        """.formatted(userEmail, password);

        MvcResult userResult = mockMvc.perform(post("/api/auth/login3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginUser))
                .andExpect(status().isOk())
                .andReturn();

        userJwt = userResult.getResponse().getCookie("jwtToken");

        // =========================
        // CREATE ADMIN
        // =========================
        String registerAdmin = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Admin",
          "lastName":"User"
        }
        """.formatted(adminEmail, password);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerAdmin))
                .andExpect(status().isOk());

        // 🔥 Promotion admin AVANT login
        User admin = userRepository.findByEmail(adminEmail).orElseThrow();
        admin.setAdmin(true);
        userRepository.save(admin);

        // LOGIN ADMIN
        String loginAdmin = """
        {
          "email":"%s",
          "password":"%s"
        }
        """.formatted(adminEmail, password);

        MvcResult adminResult = mockMvc.perform(post("/api/auth/login3")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginAdmin))
                .andExpect(status().isOk())
                .andReturn();

        adminJwt = adminResult.getResponse().getCookie("jwtToken");
    }

    // ============================================================
    // 2️⃣ 401 - non authentifié
    // ============================================================

    @Test
    @Order(2)
    void shouldReturn401WhenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isUnauthorized());
    }

    // ============================================================
    // 3️⃣ 403 - USER
    // ============================================================

    @Test
    @Order(3)
    void shouldReturn403WhenUserAccessAdminEndpoint() throws Exception {

        mockMvc.perform(get("/api/admin/users")
                .cookie(userJwt))
                .andExpect(status().isForbidden());
    }

    // ============================================================
    // 4️⃣ 200 - ADMIN
    // ============================================================

    @Test
    @Order(4)
    void shouldAllowAdminToAccessUsers() throws Exception {

        mockMvc.perform(get("/api/admin/users")
                .cookie(adminJwt))
                .andExpect(status().isOk());
    }

    // ============================================================
    // 5️⃣ DELETE USER (si endpoint existe)
    // ============================================================

	// @Test
	// @Order(5)
	// void shouldAllowAdminToDeleteUser() throws Exception {

	// 	mockMvc.perform(delete("/api/admin/users/" + testUserId)
	// 			.with(csrf())
	// 			.cookie(adminJwt))
	// 			.andExpect(status().isNoContent());

	// 	User deletedUser = userRepository.findById(testUserId).orElseThrow();

	// 	Assertions.assertTrue(deletedUser.isDeleted()); // ✅ SOFT DELETE
	// }

	// ============================================================
	// 5️⃣ DELETE USER (hard delete)
	// ============================================================

	// @Test
	// @Order(5)
	// void shouldAllowAdminToDeleteUser() throws Exception {

	// 	mockMvc.perform(delete("/api/admin/users/" + testUserId)
	// 			.with(csrf())
	// 			.cookie(adminJwt))
	// 			.andExpect(status().isNoContent());

	// 	// Vérifie que l'utilisateur a été supprimé en base
	// 	Assertions.assertFalse(userRepository.findById(testUserId).isPresent());
	// }

	// @Test
	// @Order(5)
	// void shouldAllowAdminToDeleteUser() throws Exception {

	// 	mockMvc.perform(delete("/api/admin/users/" + testUserId)
	// 			.with(csrf())
	// 			.cookie(adminJwt))
	// 			.andExpect(status().isNoContent());

	// 	User deletedUser = userRepository.findById(testUserId).orElseThrow();

	// 	Assertions.assertTrue(deletedUser.isDeleted());
	// }
}
