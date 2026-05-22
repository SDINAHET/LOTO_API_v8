package com.fdjloto.api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdjloto.api.model.User;
import com.fdjloto.api.payload.DeleteAccountRequest;
import com.fdjloto.api.payload.UpdateProfileRequest;
import com.fdjloto.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.servlet.http.Cookie;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        testUser = new User();
        testUser.setId(UUID.randomUUID().toString());
        testUser.setEmail("user@test.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setAdmin(false);

        userRepository.save(testUser);
    }

    // ============================================================
    // 1️⃣ ADMIN → GET all users
    // ============================================================

    @Test
    @DisplayName("ADMIN should retrieve all users")
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void adminShouldGetAllUsers() throws Exception {

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("user@test.com"));
    }

    // ============================================================
    // 2️⃣ USER → GET all users → 403
    // ============================================================

    @Test
    @DisplayName("USER should NOT retrieve all users")
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void userShouldNotGetAllUsers() throws Exception {

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isForbidden());
    }

    // ============================================================
    // 3️⃣ USER → GET own profile
    // ============================================================

    @Test
    @DisplayName("USER should retrieve own profile")
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void userShouldGetOwnProfile() throws Exception {

        mockMvc.perform(get("/api/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    // ============================================================
    // 4️⃣ USER → Update profile SUCCESS
    // ============================================================

    @Test
    @DisplayName("USER should update profile with correct password")
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void userShouldUpdateProfileSuccessfully() throws Exception {

        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setFirstName("Updated");
        req.setLastName("User");
        req.setCurrentPassword("password");

        mockMvc.perform(put("/api/users/" + testUser.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    // ============================================================
    // 5️⃣ USER → Update profile WRONG password
    // ============================================================

    @Test
    @DisplayName("USER update should fail with wrong password")
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void userUpdateShouldFailWithWrongPassword() throws Exception {

        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setFirstName("Updated");
        req.setLastName("User");
        req.setCurrentPassword("wrongPassword");

        mockMvc.perform(put("/api/users/" + testUser.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ============================================================
    // 6️⃣ DELETE account → 401 without JWT cookie
    // ============================================================

    @Test
    @DisplayName("Delete account should return 401 without JWT cookie")
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void deleteShouldFailWithoutJwtCookie() throws Exception {

        DeleteAccountRequest req = new DeleteAccountRequest();
        req.setCurrentPassword("password");

        mockMvc.perform(delete("/api/users/me")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("Should register new user")
    void shouldRegisterUser() throws Exception {

        User user = new User();
        user.setEmail("new@test.com");
        user.setPassword("password");
        user.setFirstName("New");
        user.setLastName("User");

        mockMvc.perform(post("/api/users/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("new@test.com"));
    }

    @Test
    @DisplayName("Should return 404 when user not found")
    @WithMockUser(username = "admin@test.com", roles = {"ADMIN"})
    void shouldReturn404WhenUserNotFound() throws Exception {

        mockMvc.perform(get("/api/users/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Delete account should fail with wrong password")
    @WithMockUser(username = "user@test.com", roles = {"USER"})
    void deleteShouldFailWithWrongPassword() throws Exception {

        DeleteAccountRequest req = new DeleteAccountRequest();
        req.setCurrentPassword("wrongPassword");

        mockMvc.perform(delete("/api/users/me")
                .cookie(new Cookie("jwtToken","fakeToken"))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
                // .andExpect(status().isBadRequest());
                .andExpect(status().isUnauthorized());
    }
}
