package com.pokedex.pokedex.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    private final String secret = Base64.getEncoder().encodeToString("mi-secreto-super-seguro-para-test-jwt-2026".getBytes());

    private UserDetails userDetails;
    private UserDetails otherUser;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", secret);
        ReflectionTestUtils.setField(jwtService, "expirationMs", 3600000L);

        userDetails = User.builder()
                .username("trainer@test.com")
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USUARIO")))
                .build();

        otherUser = User.builder()
                .username("other@test.com")
                .password("password")
                .authorities(List.of())
                .build();
    }

    @Nested
    @DisplayName("generateToken")
    class GenerateToken {

        @Test
        void generatesValidToken() {
            var token = jwtService.generateToken(userDetails);
            assertNotNull(token);
            assertFalse(token.isBlank());
        }

        @Test
        void tokenContainsExpectedClaims() {
            var token = jwtService.generateToken(userDetails);
            var claims = extractClaims(token);
            assertEquals("trainer@test.com", claims.getSubject());
            assertNotNull(claims.getIssuedAt());
            assertNotNull(claims.getExpiration());
        }
    }

    @Nested
    @DisplayName("extractUsername")
    class ExtractUsername {

        @Test
        void extractsUsernameCorrectly() {
            var token = jwtService.generateToken(userDetails);
            var username = jwtService.extractUsername(token);
            assertEquals("trainer@test.com", username);
        }
    }

    @Nested
    @DisplayName("isTokenValid")
    class IsTokenValid {

        @Test
        void returnsTrueForSameUser() {
            var token = jwtService.generateToken(userDetails);
            assertTrue(jwtService.isTokenValid(token, userDetails));
        }

        @Test
        void returnsFalseForDifferentUser() {
            var token = jwtService.generateToken(userDetails);
            assertFalse(jwtService.isTokenValid(token, otherUser));
        }

        @Test
        void returnsFalseForExpiredToken() {
            ReflectionTestUtils.setField(jwtService, "expirationMs", -1000L);
            var token = jwtService.generateToken(userDetails);
            assertFalse(jwtService.isTokenValid(token, userDetails));
        }

        @Test
        void returnsFalseForAlteredToken() {
            var token = jwtService.generateToken(userDetails);
            var alteredToken = token.substring(0, token.lastIndexOf('.') - 1) + "X" +
                    token.substring(token.lastIndexOf('.'));
            assertFalse(jwtService.isTokenValid(alteredToken, userDetails));
        }
    }

    private Claims extractClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
