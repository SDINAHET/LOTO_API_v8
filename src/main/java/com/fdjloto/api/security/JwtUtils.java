package com.fdjloto.api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static final String CLAIM_TOKEN_TYPE = "type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs; // access TTL (10 min chez toi)

    @Value("${app.refreshExpirationMs}")
    private long refreshExpirationMs; // refresh TTL (ex: 30 jours)

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // ✅ compat: ton code appelle generateJwtToken()
    public String generateJwtToken(Authentication authentication) {
        return generateAccessToken(authentication);
    }

    public String generateAccessToken(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(authentication.getName()) // email
                .claim("roles", roles)
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getKey())
                .compact();
    }

    public String generateAccessTokenFromEmailAndRoles(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_ACCESS)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getKey())
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim(CLAIM_TOKEN_TYPE, TOKEN_TYPE_REFRESH)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
                .signWith(getKey())
                .compact();
    }

    public String getUserFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody();
        return claims.get("roles", List.class);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;

        } catch (SignatureException e) {
            logger.warn("Invalid JWT signature");

        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT token");

        } catch (ExpiredJwtException e) {
            logger.debug("Expired JWT token");

        } catch (UnsupportedJwtException e) {
            logger.warn("Unsupported JWT token");

        } catch (IllegalArgumentException e) {
            logger.warn("JWT token is empty");

        } catch (WeakKeyException e) {
            logger.error("JWT key too weak");
        }

        return false;
    }

    public boolean validateAccessToken(String token) {
        if (!validateJwtToken(token)) return false;
        String type = getTokenType(token);
        // compat: si anciens tokens sans claim type, on les considère access
        return type == null || TOKEN_TYPE_ACCESS.equals(type);
    }

    public boolean validateRefreshToken(String token) {
        if (!validateJwtToken(token)) return false;
        return TOKEN_TYPE_REFRESH.equals(getTokenType(token));
    }

    private String getTokenType(String token) {
        Claims claims = Jwts.parser().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody();
        Object t = claims.get(CLAIM_TOKEN_TYPE);
        return t == null ? null : t.toString();
    }
}
