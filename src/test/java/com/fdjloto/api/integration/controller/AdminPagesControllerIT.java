package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.RefreshTokenRepository;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminPagesControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String adminToken;
    private String userToken;

    // ============================================================
    // 🔧 SETUP PROPRE (Reset DB sans erreur FK)
    // ============================================================

    @BeforeEach
    void setup() throws Exception {

        // Nettoyage robuste (ordre important)
        jdbcTemplate.execute("TRUNCATE TABLE refresh_tokens CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE users CASCADE");

        // =========================
        // Création ADMIN
        // =========================
        User admin = new User(
                "Admin",
                "Root",
                "admin@test.com",
                passwordEncoder.encode("password"),
                true
        );
        userRepository.save(admin);

        // =========================
        // Création USER
        // =========================
        User user = new User(
                "User",
                "Normal",
                "user@test.com",
                passwordEncoder.encode("password"),
                false
        );
        userRepository.save(user);

        // Login pour générer JWT
        adminToken = login("admin@test.com");
        userToken = login("user@test.com");
    }

    // ============================================================
    // 🔐 Méthode utilitaire Login
    // ============================================================

    private String login(String email) throws Exception {

        String response = mockMvc.perform(post("/api/auth/login3")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "email": "%s",
                              "password": "password"
                            }
                        """.formatted(email)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return response.split("\"token\":\"")[1].split("\"")[0];
    }

    // ============================================================
    // 1️⃣ ADMIN → 200 OK
    // ============================================================

    @Test
    @DisplayName("Admin should access dashboard")
    void shouldReturnDashboardForAdmin() throws Exception {

        mockMvc.perform(get("/admin/dashboard")
                        .cookie(new Cookie("jwtToken", adminToken)))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/admin/dashboard.html"));
    }

    // ============================================================
    // 2️⃣ NO TOKEN → 401
    // ============================================================

    @Test
    @DisplayName("Should return 401 without token")
    void shouldReturn401WithoutToken() throws Exception {

        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isUnauthorized());
    }

    // ============================================================
    // 3️⃣ USER NON ADMIN → 403
    // ============================================================

    // @Test
    // @DisplayName("User should not access dashboard")
    // void shouldReturn403ForNonAdmin() throws Exception {

    //     mockMvc.perform(get("/admin/dashboard")
    //                     .cookie(new Cookie("jwtToken", userToken)))
    //             .andExpect(status().isForbidden());
    // }

    // ============================================================
    // 4️⃣ MAUVAISE MÉTHODE → 405
    // ============================================================

    // @Test
    // @DisplayName("POST should return 405")
    // void shouldReturn405WhenWrongMethod() throws Exception {

    //     mockMvc.perform(post("/admin/dashboard")
    //                     .cookie(new Cookie("jwtToken", adminToken)))
    //             .andExpect(status().isMethodNotAllowed());
    // }

	@Test
	@DisplayName("POST should return 405")
	void shouldReturn405WhenWrongMethod() throws Exception {

		mockMvc.perform(post("/admin/dashboard")
						.with(csrf()) // ← AJOUTER ICI
						.cookie(new Cookie("jwtToken", adminToken)))
				.andExpect(status().isMethodNotAllowed());
	}

    // ============================================================
    // 5️⃣ URL INCONNUE → 404
    // ============================================================

    @Test
    @DisplayName("Unknown url should return 404")
    void shouldReturn404ForUnknownUrl() throws Exception {

        mockMvc.perform(get("/admin/dashboard-unknown")
                        .cookie(new Cookie("jwtToken", adminToken)))
                .andExpect(status().isNotFound());
    }
}
