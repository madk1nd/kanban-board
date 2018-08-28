package ru.goodgame.auth.service;

import io.jsonwebtoken.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.dto.TokenBundle;
import ru.goodgame.auth.exception.NotAuthorizedException;
import ru.goodgame.auth.exception.UserNotFoundException;
import ru.goodgame.auth.model.User;
import ru.goodgame.auth.repository.IUserRepository;
import ru.goodgame.auth.repository.ITokenRepository;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService implements IAuthService {

    @Value("${jwt.secret}")
    private String secret;

    @Nonnull private final IUserRepository userRepository;
    @Nonnull private final ITokenRepository tokenRepository;
    @Nonnull private final BCryptPasswordEncoder encoder;

    public AuthService(@Nonnull IUserRepository userRepository, @Nonnull ITokenRepository tokenRepository, @Nonnull BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
    }

    @Override
    @Nonnull
    public TokenBundle generateTokens(@Nonnull String username, @Nonnull String password) {

        @Nonnull val user = userRepository.findByUsername(username)
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

        List<String> tokens = tokenRepository.findByUserId(user.getId());
        validate(tokens, user.getId());

        tokenRepository.saveRefreshToken(user.getId(), refreshToken);

        return new TokenBundle(accessToken, refreshToken, accessTokenExpiredIn.toEpochMilli());
    }

    private void validate(List<String> tokens, UUID userId) {
        if (tokens.size() >= 9) {
            tokenRepository.deleteAllBy(userId);
        } else {
            JwtParser jwtParser = Jwts.parser().setSigningKey(secret);
            tokens.stream()
                    .filter(token -> isTokenExpired(jwtParser, token))
                    .forEach(tokenRepository::delete);
        }
    }

    private boolean isTokenExpired(JwtParser jwtParser, String token) {
        long expiresIn = Long.parseLong(jwtParser.parseClaimsJws(token).getBody().get("expiresIn").toString());
        return (expiresIn - Instant.now().toEpochMilli()) <= 0;
    }

    @Nonnull
    private String buildToken(UUID userId, Instant time) {
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
