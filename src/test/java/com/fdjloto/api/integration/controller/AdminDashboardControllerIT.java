package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fdjloto.api.integration.AbstractIntegrationTest;

/**
 * Tests d'intégration pour le contrôleur AdminDashboardController.
 *
 * Ces tests vérifient :
 *  - L'accès sécurisé aux endpoints admin
 *  - La gestion des rôles (ADMIN vs USER)
 *  - La lecture du fichier de logs
 *  - La gestion des erreurs et cas limites
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdminDashboardControllerIT extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Path LOG_PATH = Paths.get("logs/application.log");

    private String adminToken;
    private String userToken;

    /**
     * Préparation avant chaque test :
     *  - Reset complet de la base (TRUNCATE CASCADE)
     *  - Création d'un utilisateur ADMIN
     *  - Création d'un utilisateur normal
     *  - Génération des tokens JWT
     *  - Création d'un fichier log de test
     */
    @BeforeEach
    void setup() throws Exception {

        jdbcTemplate.execute("TRUNCATE TABLE refresh_tokens CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE users CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE tickets CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE ticket_gains CASCADE");

        // Création ADMIN
        User admin = new User(
                "Admin",
                "Root",
                "admin@test.com",
                passwordEncoder.encode("password"),
                true
        );
        userRepository.save(admin);

        // Création USER normal
        User user = new User(
                "User",
                "Normal",
                "user@test.com",
                passwordEncoder.encode("password"),
                false
        );
        userRepository.save(user);

        adminToken = login("admin@test.com");
        userToken = login("user@test.com");

        Files.createDirectories(LOG_PATH.getParent());

        Files.write(LOG_PATH,
                List.of("line1", "line2", "line3", "line4", "line5"),
                StandardCharsets.UTF_8
        );
    }

    /**
     * Nettoyage du fichier log après chaque test.
     */
    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(LOG_PATH);
    }

    /**
     * Méthode utilitaire permettant de récupérer un JWT via l'endpoint login.
     */
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

    /**
     * ✅ Vérifie qu'un ADMIN authentifié peut accéder à /admin/ping.
     *
     * Attendu :
     *  - HTTP 200
     *  - Contenu = "admin ok"
     */
    @Test
    @Order(1)
    void shouldReturnAdminOk() throws Exception {

        mockMvc.perform(get("/admin/ping")
                        .cookie(new Cookie("jwtToken", adminToken)))
                .andExpect(status().isOk())
                .andExpect(content().string("admin ok"));
    }

    /**
     * ✅ Vérifie que l'ADMIN peut consulter les logs.
     *
     * Attendu :
     *  - HTTP 200
     *  - Le contenu contient la dernière ligne du fichier log
     */
    @Test
    @Order(2)
    void shouldReturnLogs() throws Exception {

        mockMvc.perform(get("/admin/logs")
                        .cookie(new Cookie("jwtToken", adminToken)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("line5")));
    }

    /**
     * ✅ Vérifie que le paramètre lines négatif ne provoque pas d'erreur.
     *
     * Attendu :
     *  - HTTP 200
     *  - Le contrôleur gère proprement les valeurs invalides
     */
    @Test
    @Order(3)
    void shouldHandleNegativeLines() throws Exception {

        mockMvc.perform(get("/admin/logs?lines=-5")
                        .cookie(new Cookie("jwtToken", adminToken)))
                .andExpect(status().isOk());
    }

    /**
     * ✅ Vérifie que le paramètre lines très élevé est limité correctement.
     *
     * Attendu :
     *  - HTTP 200
     *  - Pas d'exception liée à une lecture excessive
     */
    @Test
    @Order(4)
    void shouldLimitMaxLines() throws Exception {

        mockMvc.perform(get("/admin/logs?lines=999999")
                        .cookie(new Cookie("jwtToken", adminToken)))
                .andExpect(status().isOk());
    }

    /**
     * ✅ Vérifie le comportement si le fichier log est absent.
     *
     * Attendu :
     *  - HTTP 200
     *  - Message explicite : "Fichier de log introuvable"
     */
    @Test
    @Order(5)
    void shouldReturnMessageIfFileMissing() throws Exception {

        Files.deleteIfExists(LOG_PATH);

        mockMvc.perform(get("/admin/logs")
                        .cookie(new Cookie("jwtToken", adminToken)))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("Fichier de log introuvable")));
    }

    /**
     * ❌ Vérifie qu'un accès sans token retourne 401 Unauthorized.
     */
    @Test
    @Order(6)
    void shouldReturn401WithoutToken() throws Exception {

        mockMvc.perform(get("/admin/ping"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * ❌ Vérifie qu'un utilisateur non-admin reçoit 403 Forbidden.
     */
    @Test
    @Order(7)
    void shouldReturn403IfNotAdmin() throws Exception {

        mockMvc.perform(get("/admin/ping")
                        .cookie(new Cookie("jwtToken", userToken)))
                .andExpect(status().isForbidden());
    }
}
