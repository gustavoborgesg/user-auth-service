package com.example.auth.infra.security.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.auth.infra.entities.User;
import com.example.auth.infra.exceptions.CustomExceptionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.cookie.name}")
    private String cookieName;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(cookieName)
                    .withSubject(user.getPerson().getCpf())
                    .withClaim("role", user.getRole().getRoleName())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new CustomExceptionRequest("Error while validating token!", "", HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
    }

    public DecodedJWT validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer(cookieName)
                    .build()
                    .verify(token);
        } catch (TokenExpiredException exception) {
            throw new CustomExceptionRequest("Token is expired!", "", HttpStatus.UNAUTHORIZED, false);
        } catch (JWTVerificationException exception) {
            throw new CustomExceptionRequest("Error while validating token!", "", HttpStatus.INTERNAL_SERVER_ERROR, false);
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }
}
