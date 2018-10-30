package ru.goodgame.auth.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class JwtTokenBuilder implements ITokenBuilder {

    @Value("${jwt.secret}")
    private String secret;

    @Nonnull private final JwtParser parser = Jwts.parser();

    @Nonnull
    @Override
    public String buildToken(@Nonnull UUID userId, @Nonnull Instant time) {
        @Nonnull val claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("expiresIn", time.toEpochMilli());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public boolean invalid(@Nonnull String token) {
        @Nonnull val now = Instant.now();
        try {
            @Nonnull val claimsJws = parser.setSigningKey(secret).parseClaimsJws(token);
            return ((Long) claimsJws.getBody().get("expiresIn")) < now.toEpochMilli();
        } catch (JwtException e) {
            log.error("Token invalid, cause :: {}", e.getMessage());
            return true;
        }
    }

    @Nonnull
    @Override
    public String getClaim(@Nonnull String token, @Nonnull String claimName) {
        @Nonnull val claimsJws = parser.setSigningKey(secret).parseClaimsJws(token);
        return claimsJws.getBody().get(claimName).toString();
    }

    // Only for testing purposes
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
