// package com.fdjloto.api.integration;

// import jakarta.servlet.http.Cookie;
// import org.junit.jupiter.api.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MvcResult;

// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// class SecurityIntegrationIT extends BaseIntegrationTest {

//     private static Cookie userJwtCookie;
//     private static Cookie adminJwtCookie;

//     // ============================
//     // 1️⃣ CREATE USER + ADMIN
//     // ============================

//     @Test
//     @Order(1)
//     void setupUsers() throws Exception {

//         String userEmail = "user" + System.currentTimeMillis() + "@loto.com";
//         String adminEmail = "admin" + System.currentTimeMillis() + "@loto.com";
//         String password = "password123";

//         // --- Register USER
//         String registerUser = """
//         {
//           "email":"%s",
//           "password":"%s",
//           "firstName":"User",
//           "lastName":"Test"
//         }
//         """.formatted(userEmail, password);

//         mockMvc.perform(post("/api/auth/register")
//                 .with(csrf())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(registerUser))
//                 .andExpect(status().isOk());

//         // --- Login USER
//         String loginUser = """
//         {
//           "email":"%s",
//           "password":"%s"
//         }
//         """.formatted(userEmail, password);

//         MvcResult userResult = mockMvc.perform(post("/api/auth/login3")
//                 .with(csrf())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(loginUser))
//                 .andExpect(status().isOk())
//                 .andExpect(cookie().exists("jwtToken"))
//                 .andReturn();

//         userJwtCookie = userResult.getResponse().getCookie("jwtToken");


//         // ⚠️ ADMIN : adapte si tu as un endpoint spécial ou rôle auto
//         String registerAdmin = """
//         {
//           "email":"%s",
//           "password":"%s",
//           "firstName":"Admin",
//           "lastName":"Test"
//         }
//         """.formatted(adminEmail, password);

//         mockMvc.perform(post("/api/auth/register")
//                 .with(csrf())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(registerAdmin))
//                 .andExpect(status().isOk());

//         // 🔥 Ici ton système doit donner ROLE_ADMIN
//         // Si pas automatique → adapte selon ton projet

//         String loginAdmin = """
//         {
//           "email":"%s",
//           "password":"%s"
//         }
//         """.formatted(adminEmail, password);

//         MvcResult adminResult = mockMvc.perform(post("/api/auth/login3")
//                 .with(csrf())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(loginAdmin))
//                 .andExpect(status().isOk())
//                 .andExpect(cookie().exists("jwtToken"))
//                 .andReturn();

//         adminJwtCookie = adminResult.getResponse().getCookie("jwtToken");
//     }

//     // ============================
//     // 2️⃣ 401 TEST
//     // ============================

//     @Test
//     @Order(2)
//     void shouldReturn401WhenNoJwtProvided() throws Exception {

//         mockMvc.perform(get("/api/tickets"))
//                 .andExpect(status().isUnauthorized());
//     }

//     // ============================
//     // 3️⃣ 403 TEST
//     // ============================

//     @Test
//     @Order(3)
//     void shouldReturn403ForUserAccessingAdmin() throws Exception {

//         mockMvc.perform(get("/api/admin/users")
//                 .cookie(userJwtCookie))
//                 .andExpect(status().isForbidden());
//     }

//     // ============================
//     // 4️⃣ ADMIN OK
//     // ============================

//     @Test
//     @Order(4)
//     void shouldAllowAdminAccess() throws Exception {

//         mockMvc.perform(get("/api/admin/users")
//                 .cookie(adminJwtCookie))
//                 .andExpect(status().isOk());
//     }

//     // ============================
//     // 5️⃣ SWAGGER PROTECTED
//     // ============================

//     @Test
//     @Order(5)
//     void swaggerShouldBeProtected() throws Exception {

//         mockMvc.perform(get("/swagger-ui/index.html"))
//                 .andExpect(status().isUnauthorized());
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
 * Tests d’intégration de la sécurité :
 *
 * ✔ Authentification JWT
 * ✔ Gestion des rôles
 * ✔ 401 (non authentifié)
 * ✔ 403 (rôle insuffisant)
 * ✔ Accès admin
 * ✔ Protection Swagger
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SecurityIntegrationIT extends BaseIntegrationTest {

    private static Cookie userJwtCookie;
    private static Cookie adminJwtCookie;

    @Autowired
    private UserRepository userRepository;

    // ============================================================
    // 1️⃣ SETUP : Création USER + ADMIN + génération JWT
    // ============================================================

    @Test
    @Order(1)
    void setupUsers() throws Exception {

        String userEmail = "user" + System.currentTimeMillis() + "@loto.com";
        String adminEmail = "admin" + System.currentTimeMillis() + "@loto.com";
        String password = "password123";

        // ====================================================
        // 🔹 CREATION UTILISATEUR STANDARD
        // ====================================================

        String registerUser = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"User",
          "lastName":"Test"
        }
        """.formatted(userEmail, password);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerUser))
                .andExpect(status().isOk());

        // 🔹 LOGIN USER → génération JWT ROLE_USER
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
                .andExpect(cookie().exists("jwtToken"))
                .andReturn();

        userJwtCookie = userResult.getResponse().getCookie("jwtToken");


        // ====================================================
        // 🔹 CREATION ADMIN
        // ====================================================

        String registerAdmin = """
        {
          "email":"%s",
          "password":"%s",
          "firstName":"Admin",
          "lastName":"Test"
        }
        """.formatted(adminEmail, password);

        mockMvc.perform(post("/api/auth/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerAdmin))
                .andExpect(status().isOk());

        // 🔥 IMPORTANT :
        // On force le flag admin AVANT de faire le login.
        // Sinon le JWT contiendra encore ROLE_USER.

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow();

        admin.setAdmin(true); // <-- Ton modèle utilise boolean admin
        userRepository.save(admin);

        // 🔹 LOGIN ADMIN → JWT contient maintenant ROLE_ADMIN
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
                .andExpect(cookie().exists("jwtToken"))
                .andReturn();

        adminJwtCookie = adminResult.getResponse().getCookie("jwtToken");
    }

    // ============================================================
    // 2️⃣ TEST 401 – accès sans JWT
    // ============================================================

    @Test
    @Order(2)
    void shouldReturn401WhenNoJwtProvided() throws Exception {

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isUnauthorized());
    }

    // ============================================================
    // 3️⃣ TEST 403 – USER tente accès ADMIN
    // ============================================================

    @Test
    @Order(3)
    void shouldReturn403ForUserAccessingAdmin() throws Exception {

        mockMvc.perform(get("/api/admin/users")
                .cookie(userJwtCookie))
                .andExpect(status().isForbidden());
    }

    // ============================================================
    // 4️⃣ TEST ADMIN OK
    // ============================================================

    @Test
    @Order(4)
    void shouldAllowAdminAccess() throws Exception {

        mockMvc.perform(get("/api/admin/users")
                .cookie(adminJwtCookie))
                .andExpect(status().isOk());
    }

    // ============================================================
    // 5️⃣ SWAGGER PROTECTED
    // ============================================================

    @Test
    @Order(5)
    void swaggerShouldBeProtected() throws Exception {

        // ⚠️ Avec formLogin actif → redirection 302 vers login page
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isFound()); // 302
    }
}
