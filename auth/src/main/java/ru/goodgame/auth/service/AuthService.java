package ru.goodgame.auth.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.dto.TokenBundle;
import ru.goodgame.auth.exception.InvalidRefreshTokenException;
import ru.goodgame.auth.exception.NotAuthorizedException;
import ru.goodgame.auth.exception.UserNotFoundException;
import ru.goodgame.auth.model.Token;
import ru.goodgame.auth.model.User;
import ru.goodgame.auth.repository.IUserRepository;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.google.common.hash.Hashing.sha256;

@Service
@Slf4j
public class AuthService implements IAuthService {

    @Value("${jwt.secret}")
    private String secret;

    @Nonnull private final IUserRepository userRepository;
    @Nonnull private final BCryptPasswordEncoder encoder;
    @Nonnull private final JwtParser parser;

    public AuthService(@Nonnull IUserRepository userRepository, @Nonnull BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.parser = Jwts.parser();
    }

    @Override
    @Nonnull
    public TokenBundle generateTokens(@Nonnull String username, @Nonnull String password, String remoteHost) {
        @Nonnull val user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username=" + username + " not found"));
        checkPassword(password, user);
        return buildTokens(user, remoteHost);
    }

    @Nonnull
    @Override
    public TokenBundle updateTokens(@Nonnull String token, @Nonnull String remoteAddr) {
        if (invalid(token)) {
            throw new InvalidRefreshTokenException("Invalid or expired refresh token");
        }
        @Nonnull val user = userRepository.findByUserId(userIdFrom(token))
                .orElseThrow(() -> new UserNotFoundException("Refresh token owner not found"));
        return buildTokens(user, remoteAddr);
    }

    @Nonnull
    String userIdFrom(@Nonnull String token) {
        @Nonnull val claimsJws = parser.setSigningKey(secret).parseClaimsJws(token);
        return claimsJws.getBody().get("userId").toString();
    }

    private boolean invalid(@Nonnull String token) {
        @Nonnull val now = Instant.now();
        try {
            @Nonnull val claimsJws = parser.setSigningKey(secret).parseClaimsJws(token);
            return ((Long) claimsJws.getBody().get("expiresIn")) < now.toEpochMilli();
        } catch (JwtException e) {
            log.error("Token invalid, cause :: {}", e.getMessage());
            return true;
        }
    }

    void checkPassword(@Nonnull String password, @Nonnull User user) {
        @Nonnull val hash = sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        if (!encoder.matches(hash, user.getPassword())) {
            throw new NotAuthorizedException("invalid password");
        }
    }

    @Nonnull
    private TokenBundle buildTokens(@Nonnull User user, @Nonnull String remoteHost) {
        @Nonnull val currentTime = Instant.now();

        @Nonnull val accessTokenExpiredIn = currentTime.plus(30, ChronoUnit.MINUTES);
        @Nonnull val accessToken = buildToken(user.getId(), accessTokenExpiredIn);
        @Nonnull val refreshToken = buildToken(user.getId(), currentTime.plus(60, ChronoUnit.DAYS));
        updateRefreshToken(user, remoteHost, refreshToken);

        return new TokenBundle(accessToken, refreshToken, accessTokenExpiredIn.toEpochMilli());
    }

    private void updateRefreshToken(@Nonnull User user, @Nonnull String remoteHost, @Nonnull String refreshToken) {

//        TODO: remove to another module or simple CronTab task with python
        user.getTokens().removeIf(token -> invalid(token.getToken()));

        boolean tokenExist = user.getTokens().stream()
                .map(Token::getRemoteIp)
                .anyMatch(ip -> ip.equals(remoteHost));

        if (tokenExist) {
            userRepository.updateRefreshToken(user, refreshToken, remoteHost);
        } else {
            userRepository.saveRefreshToken(user, refreshToken, remoteHost);
        }
    }

    @Nonnull
    String buildToken(@Nonnull UUID userId, @Nonnull Instant time) {
        @Nonnull val claims = Jwts.claims();
        claims.put("userId", userId);
        claims.put("expiresIn", time.toEpochMilli());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // only for testing purposes
    void setSecret(@Nonnull String secret) {
        this.secret = secret;
    }
}
