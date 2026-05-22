package com.fdjloto.api.integration.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityLogControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private UsernamePasswordAuthenticationToken buildAuth() {
        return new UsernamePasswordAuthenticationToken(
                "admin@loto.com",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }

    // ============================================================
    // 1️⃣ 200 OK – événement complet
    // ============================================================

    @Test
    @DisplayName("Should log security event with all parameters")
    void shouldLogSecurityEvent() throws Exception {

        mockMvc.perform(
                post("/api/security/event")
                        .with(authentication(buildAuth()))
                        .param("type", "LOGIN_SUCCESS")
                        .param("user", "test@loto.com")
                        .param("reason", "Authenticated")
                        .header("User-Agent", "JUnit-Test")
                        .header("X-Forwarded-For", "192.168.1.10")
        )
        .andExpect(status().isOk());
    }

    // ============================================================
    // 2️⃣ 200 OK – paramètres optionnels absents
    // ============================================================

    @Test
    @DisplayName("Should log event with only required type parameter")
    void shouldLogWithOnlyType() throws Exception {

        mockMvc.perform(
                post("/api/security/event")
                        .with(authentication(buildAuth()))
                        .param("type", "TOKEN_REFRESH")
        )
        .andExpect(status().isOk());
    }

    // ============================================================
    // 3️⃣ 400 BAD REQUEST – type manquant
    // ============================================================

    @Test
    @DisplayName("Should return 400 when type is missing")
    void shouldReturn400IfTypeMissing() throws Exception {

        mockMvc.perform(
                post("/api/security/event")
                        .with(authentication(buildAuth()))
                        .param("user", "test@loto.com")
        )
        .andExpect(status().isBadRequest());
    }

    // ============================================================
    // 4️⃣ 200 OK – IP fallback remoteAddr
    // ============================================================

    @Test
    @DisplayName("Should fallback to remote address if no proxy headers")
    void shouldUseRemoteAddrIfNoHeaders() throws Exception {

        mockMvc.perform(
                post("/api/security/event")
                        .with(authentication(buildAuth()))
                        .param("type", "ACCESS_DENIED")
                        .with(request -> {
                            request.setRemoteAddr("127.0.0.1");
                            return request;
                        })
        )
        .andExpect(status().isOk());
    }
}
