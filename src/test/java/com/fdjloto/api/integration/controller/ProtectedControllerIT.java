package com.fdjloto.api.integration.controller;

import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProtectedControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    // ============================================================
    // 1️⃣ 200 OK – JWT valide
    // ============================================================

    @Test
    @DisplayName("Should return user info when authenticated")
    void shouldReturnUserInfo() throws Exception {

        userRepository.deleteAll();

        User user = new User();
        user.setEmail("test@loto.com");
        user.setPassword("encoded-password"); // obligatoire
        user.setFirstName("Stephane");
        user.setLastName("Dinahet");
        user.setAdmin(false); // ✅ correspond à ta DB

        userRepository.save(user);

        var auth = new UsernamePasswordAuthenticationToken(
                "test@loto.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        mockMvc.perform(get("/api/protected/userinfo")
                        .with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token_valid").value("✅"))
                .andExpect(jsonPath("$.username").value("Stephane Dinahet"))
                .andExpect(jsonPath("$.email").value("test@loto.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }

    // ============================================================
    // 2️⃣ 401 – pas authentifié
    // ============================================================

    @Test
    @DisplayName("Should return 401 if not authenticated")
    void shouldReturn401IfNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/protected/userinfo"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    // ============================================================
    // 3️⃣ 404 – utilisateur introuvable
    // ============================================================

    @Test
    @DisplayName("Should return 404 if user not found")
    void shouldReturn404IfUserNotFound() throws Exception {

        userRepository.deleteAll();

        var auth = new UsernamePasswordAuthenticationToken(
                "unknown@loto.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        mockMvc.perform(get("/api/protected/userinfo")
                        .with(authentication(auth)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("User not found"));
    }
}
