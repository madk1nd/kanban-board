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
    public String buildToken(@Nonnull final UUID userId,
                             @Nonnull final Instant time) {
        val claims = Jwts.claims();

        claims.put("userId", userId);
        claims.put("expiresIn", time.toEpochMilli());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @Override
    public boolean invalid(@Nonnull final String token) {
        val now = Instant.now();
        try {
            val claimsJws = parser
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            return ((Long) claimsJws.getBody().get("expiresIn")) < now.toEpochMilli();
        } catch (JwtException e) {
            log.error("Token invalid, cause :: {}", e.getMessage());
            return true;
        }
    }

    @Nonnull
    @Override
    public String getClaim(@Nonnull final String token,
                           @Nonnull final String claimName) {
        return parser
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .get(claimName)
                .toString();
    }

    // Only for testing purposes
    public void setSecret(final String secret) {
        this.secret = secret;
    }
}
