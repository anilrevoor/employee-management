package employee_management.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    private static final String SECRET_KEY =
            "employee-management-secret-key-for-jwt-2026";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void generateToken_shouldReturnToken() {

        String token = jwtService.generateToken("admin", "ADMIN");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void extractUsername_shouldReturnUsername() {

        String token = jwtService.generateToken("admin", "ADMIN");

        String username = jwtService.extractUsername(token);

        assertEquals("admin", username);
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {

        String token = jwtService.generateToken("admin", "ADMIN");

        boolean result =
                jwtService.isTokenValid(token, "admin");

        assertTrue(result);
    }

    @Test
    void isTokenValid_shouldReturnFalseForDifferentUsername() {

        String token = jwtService.generateToken("admin", "ADMIN");

        boolean result =
                jwtService.isTokenValid(token, "user");

        assertFalse(result);
    }

    @Test
    void extractUsername_shouldThrowExceptionForInvalidToken() {

        assertThrows(Exception.class, () ->
                jwtService.extractUsername("invalid-token")
        );
    }

    @Test
    void isTokenValid_shouldThrowExceptionForExpiredToken() {

        String expiredToken = Jwts.builder()
                .subject("admin")
                .issuedAt(new Date(System.currentTimeMillis() - 5000))
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(getSigningKey())
                .compact();

        assertThrows(Exception.class, () ->
                jwtService.isTokenValid(
                        expiredToken,
                        "admin"
                )
        );
    }
}