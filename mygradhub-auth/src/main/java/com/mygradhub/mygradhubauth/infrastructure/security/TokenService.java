package com.mygradhub.mygradhubauth.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mygradhub.mygradhubauth.domain.exception.InvalidTokenException;
import com.mygradhub.mygradhubauth.domain.exception.TokenExpiredCustomException;
import com.mygradhub.mygradhubauth.shared.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {

    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    public String generateToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.create()
                    .withIssuer(jwtIssuer)
                    .withSubject(username)
                    .withExpiresAt(generateExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new AuthenticationServiceException(AppConstants.ERROR_WHILE_GENERATING_TOKEN, e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            return JWT.require(algorithm)
                    .withIssuer(jwtIssuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    private Instant generateExpiration() {
        return Instant.now().plusMillis(jwtExpirationMs);
    }
    public boolean isValid(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
            JWT.require(algorithm)
                    .withIssuer(jwtIssuer)
                    .build()
                    .verify(token);
            return true;
        } catch (TokenExpiredException e) {
            throw new TokenExpiredCustomException(AppConstants.EXPIRED_TOKEN, e);
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException(AppConstants.INVALID_TOKEN, e);
        }
    }
}
