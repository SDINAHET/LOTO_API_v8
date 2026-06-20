
// package com.fdjloto.api.security;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import io.jsonwebtoken.security.Keys;
// import org.junit.jupiter.api.Test;
// import org.springframework.test.util.ReflectionTestUtils;

// import javax.crypto.SecretKey;
// import java.nio.charset.StandardCharsets;
// import java.util.Date;

// import static org.junit.jupiter.api.Assertions.*;

// class JwtUtilsTest {

//     @Test void generateAndValidateToken_roundTrip() {
//         JwtUtils jwt = new JwtUtils();

//         // Set a deterministic secret key for tests
//         String secret = "0123456789abcdef0123456789abcdef"; // 32 chars
//         ReflectionTestUtils.setField(jwt, "jwtSecret", secret);
//         ReflectionTestUtils.setField(jwt, "jwtExpirationMs", 60_000);

//         String token = jwt.generateJwtToken("user@example.com", "u1", "user");
//         assertNotNull(token);
//         assertTrue(jwt.validateJwtToken(token));

//         assertEquals("user@example.com", jwt.getUserNameFromJwtToken(token));
//         assertEquals("u1", jwt.getUserIdFromJwtToken(token));
//         assertEquals("user", jwt.getUserRoleFromJwtToken(token));
//     }

//     @Test void validateJwtToken_invalidToken_returnsFalse() {
//         JwtUtils jwt = new JwtUtils();
//         ReflectionTestUtils.setField(jwt, "jwtSecret", "0123456789abcdef0123456789abcdef");
//         assertFalse(jwt.validateJwtToken("not-a-jwt"));
//     }

//     @Test void validateJwtToken_expired_returnsFalse() {
//         JwtUtils jwt = new JwtUtils();
//         String secret = "0123456789abcdef0123456789abcdef";
//         ReflectionTestUtils.setField(jwt, "jwtSecret", secret);

//         SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//         String token = Jwts.builder()
//             .subject("user@example.com")
//             .claim("userId", "u1")
//             .claim("role", "user")
//             .issuedAt(new Date(System.currentTimeMillis() - 10_000))
//             .expiration(new Date(System.currentTimeMillis() - 1))
//             .signWith(key)
//             .compact();

//         assertFalse(jwt.validateJwtToken(token));
//     }
// }





package com.fdjloto.api.unit.security;

import com.fdjloto.api.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() throws Exception {
        jwtUtils = new JwtUtils();

        setPrivateField(jwtUtils, "jwtSecret",
                "0123456789012345678901234567890123456789012345678901234567891234");
        setPrivateField(jwtUtils, "jwtExpirationMs", 600000L);
        setPrivateField(jwtUtils, "refreshExpirationMs", 3600000L);
    }

    @Test
    void shouldGenerateAndValidateAccessToken() {
        var auth = new UsernamePasswordAuthenticationToken(
                "user@loto.local",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtUtils.generateJwtToken(auth);

        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertTrue(jwtUtils.validateAccessToken(token));
        assertFalse(jwtUtils.validateRefreshToken(token));
        assertEquals("user@loto.local", jwtUtils.getUserFromJwtToken(token));
        assertEquals(List.of("ROLE_USER"), jwtUtils.getRolesFromJwtToken(token));
    }

    @Test
    void shouldGenerateAndValidateRefreshToken() {
        String token = jwtUtils.generateRefreshToken("user@loto.local");

        assertNotNull(token);
        assertTrue(jwtUtils.validateJwtToken(token));
        assertFalse(jwtUtils.validateAccessToken(token));
        assertTrue(jwtUtils.validateRefreshToken(token));
        assertEquals("user@loto.local", jwtUtils.getUserFromJwtToken(token));
    }

    @Test
    void shouldRejectMalformedToken() {
        assertFalse(jwtUtils.validateJwtToken("bad.token.value"));
    }

    @Test
    void shouldRejectEmptyToken() {
        assertFalse(jwtUtils.validateJwtToken(""));
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
