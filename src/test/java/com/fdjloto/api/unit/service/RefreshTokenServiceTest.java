// package com.fdjloto.api.service;

// import org.junit.jupiter.api.*;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.*;
// import com.fdjloto.api.repository.RefreshTokenRepository;
// import com.fdjloto.api.repository.UserRepository;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
// class RefreshTokenServiceTest {
//     @Mock RefreshTokenRepository refreshTokenRepository;
//     @Mock UserRepository userRepository;

//     @InjectMocks RefreshTokenService service;

// @Test void smoke_01() {
//             assertNotNull(service);
//         }
// // @Test void smoke_02() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_03() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_04() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_05() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_06() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_07() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_08() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_09() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_10() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_11() {
// //             assertNotNull(service);
// //         }
// // @Test void smoke_12() {
// //             assertNotNull(service);
// //         }
// }




package com.fdjloto.api.unit.service;

import com.fdjloto.api.model.RefreshToken;
import com.fdjloto.api.model.User;
import com.fdjloto.api.repository.RefreshTokenRepository;
import com.fdjloto.api.service.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void shouldHashRefreshToken() {
        String hash = refreshTokenService.hash("my-refresh-token");

        assertNotNull(hash);
        assertFalse(hash.isBlank());
        assertNotEquals("my-refresh-token", hash);
    }

    @Test
    void shouldGenerateSameHashForSameToken() {
        String hash1 = refreshTokenService.hash("same-token");
        String hash2 = refreshTokenService.hash("same-token");

        assertEquals(hash1, hash2);
    }

    @Test
    void shouldSaveRefreshToken() {
        User user = new User();
        user.setId("user-1");
        user.setEmail("test@loto.local");

        Instant expiresAt = Instant.now().plusSeconds(3600);

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken result = refreshTokenService.save(user, "raw-token", expiresAt);

        assertNotNull(result.getId());
        assertEquals(user, result.getUser());
        assertEquals(expiresAt, result.getExpiresAt());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getTokenHash());
        assertFalse(result.isRevoked());

        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void shouldRevokeRefreshToken() {
        RefreshToken token = new RefreshToken();
        token.setId("token-1");
        token.setRevokedAt(null);

        refreshTokenService.revoke(token);

        assertTrue(token.isRevoked());
        assertNotNull(token.getRevokedAt());

        verify(refreshTokenRepository).save(token);
    }
}
