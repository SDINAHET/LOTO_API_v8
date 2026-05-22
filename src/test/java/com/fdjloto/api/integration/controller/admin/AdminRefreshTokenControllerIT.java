package com.fdjloto.api.controller.admin;

import com.fdjloto.api.model.RefreshToken;
import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AdminRefreshTokenControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    /**
     * Test liste tokens admin
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnRefreshTokens() throws Exception {

        User user = new User();
        // user.setId(UUID.randomUUID());
		user.setId(UUID.randomUUID().toString());

        RefreshToken token1 = new RefreshToken();
        token1.setId("token1");
        token1.setTokenHash("hash1");
        token1.setUser(user);
        token1.setCreatedAt(Instant.now());
        token1.setExpiresAt(Instant.now().plusSeconds(3600));

        RefreshToken token2 = new RefreshToken();
        token2.setId("token2");
        token2.setTokenHash("hash2");
        token2.setCreatedAt(Instant.now().minusSeconds(3600));
        token2.setExpiresAt(Instant.now());
        token2.setRevokedAt(Instant.now());

        when(refreshTokenRepository.findAll()).thenReturn(List.of(token1, token2));

        mockMvc.perform(get("/api/admin/refresh-tokens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].tokenHash", notNullValue()))
                .andExpect(jsonPath("$[0].createdAt", notNullValue()))
                .andExpect(jsonPath("$[0].expiresAt", notNullValue()))
                .andExpect(jsonPath("$[0].revoked").value(false));
    }

    /**
     * Test userId null si user absent
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldHandleNullUser() throws Exception {

        RefreshToken token = new RefreshToken();
        token.setId("token3");
        token.setTokenHash("hash3");
        token.setCreatedAt(Instant.now());

        when(refreshTokenRepository.findAll()).thenReturn(List.of(token));

        mockMvc.perform(get("/api/admin/refresh-tokens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").doesNotExist());
    }

    /**
     * Sécurité : USER non admin
     */
    @Test
    @WithMockUser(roles = "USER")
    void shouldReturnForbiddenForNonAdmin() throws Exception {

        mockMvc.perform(get("/api/admin/refresh-tokens"))
                .andExpect(status().isForbidden());
    }
}
