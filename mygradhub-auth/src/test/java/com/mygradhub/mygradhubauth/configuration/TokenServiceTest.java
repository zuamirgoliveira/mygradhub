package com.mygradhub.mygradhubauth.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.mygradhub.mygradhubauth.domain.exception.InvalidTokenException;
import com.mygradhub.mygradhubauth.domain.exception.TokenExpiredCustomException;
import com.mygradhub.mygradhubauth.infrastructure.security.TokenService;
import com.mygradhub.mygradhubauth.shared.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationServiceException;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TokenServiceTest {

    private TokenService tokenService;

    private final String secret = "test-secret-key";
    private final long expirationMs = 3600000; // 1 hour
    private final String issuer = "test-issuer";

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();

        injectValue(tokenService, "jwtSecret", secret);
        injectValue(tokenService, "jwtExpirationMs", expirationMs);
        injectValue(tokenService, "jwtIssuer", issuer);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String username = "testuser";
        String token = tokenService.generateToken(username);
        assertThat(token).isNotNull();

        String validated = tokenService.validateToken(token);
        assertThat(validated).isEqualTo(username);
    }

    @Test
    void shouldThrowInvalidTokenExceptionForInvalidToken() {
        String fakeToken = "invalid.token.value";

        assertThatThrownBy(() -> tokenService.isValid(fakeToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining(AppConstants.INVALID_TOKEN);
    }

    @Test
    void shouldThrowExpiredTokenException() {
        String username = "testuser";
        Instant expiredTime = Instant.now().minusSeconds(60);

        String expiredToken = JWT.create()
                .withIssuer(issuer)
                .withSubject(username)
                .withExpiresAt(expiredTime)
                .sign(Algorithm.HMAC256(secret));

        assertThatThrownBy(() -> tokenService.isValid(expiredToken))
                .isInstanceOf(TokenExpiredCustomException.class)
                .hasMessageContaining(AppConstants.EXPIRED_TOKEN);
    }

    private void injectValue(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
