package ru.goodgame.auth.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.dto.TokenBundle;
import ru.goodgame.auth.exception.NotAuthorizedException;
import ru.goodgame.auth.exception.UserNotFoundException;
import ru.goodgame.auth.model.User;
import ru.goodgame.auth.repository.IAuthRepository;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService implements IAuthService {

    @Value("${jwt.secret}")
    private String secret;

    @Nonnull private final IAuthRepository repository;
    @Nonnull private final BCryptPasswordEncoder encoder;

    public AuthService(@Nonnull IAuthRepository repository, @Nonnull BCryptPasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    @Nonnull
    public TokenBundle generateTokens(@Nonnull String username, @Nonnull String password) {

        @Nonnull val user = repository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);

        if (!encoder.matches(password, user.getPassword())) {
            throw new NotAuthorizedException();
        }

        return buildTokens(user);
    }

    @Nonnull
    private TokenBundle buildTokens(@Nonnull User user) {
        @Nonnull val currentTime = Instant.now();

        @Nonnull val accessTokenExpiredIn = currentTime.plus(30, ChronoUnit.MINUTES);
        @Nonnull val accessToken = buildToken(user.getId(), accessTokenExpiredIn);
        @Nonnull val refreshToken = buildToken(user.getId(), currentTime.plus(60, ChronoUnit.DAYS));

        return new TokenBundle(accessToken, refreshToken, accessTokenExpiredIn.toEpochMilli());
    }

    @Nonnull
    private String buildToken(String userId, Instant time) {
        @Nonnull val claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("expiresIn", time.toEpochMilli());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // only for testing purposes
    void setSecret(String secret) {
        this.secret = secret;
    }
}
