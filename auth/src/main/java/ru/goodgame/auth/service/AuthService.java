package ru.goodgame.auth.service;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.dto.TokenBundle;
import ru.goodgame.auth.exception.InvalidRefreshTokenException;
import ru.goodgame.auth.exception.NotAuthorizedException;
import ru.goodgame.auth.exception.UserDisabledException;
import ru.goodgame.auth.exception.UserNotFoundException;
import ru.goodgame.auth.model.Token;
import ru.goodgame.auth.model.User;
import ru.goodgame.auth.repository.ITokenRepository;
import ru.goodgame.auth.repository.IUserRepository;
import ru.goodgame.auth.utils.ITokenBuilder;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.google.common.hash.Hashing.sha256;

@Service
@Slf4j
public class AuthService implements IAuthService {

    @Nonnull private final IUserRepository userRepository;
    @Nonnull private final ITokenRepository tokenRepository;
    @Nonnull private final BCryptPasswordEncoder encoder;
    @Nonnull private final ITokenBuilder tokenBuilder;

    public AuthService(@Nonnull IUserRepository userRepository,
                       @Nonnull ITokenRepository tokenRepository,
                       @Nonnull BCryptPasswordEncoder encoder,
                       @Nonnull ITokenBuilder tokenBuilder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
        this.tokenBuilder = tokenBuilder;
    }

    @Override
    @Nonnull
    public TokenBundle generateTokens(@Nonnull String username, @Nonnull String password, String remoteHost) {
        @Nonnull val user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username=" + username + " not found"));
        checkPassword(password, user);
        if (!user.getEnabled()) throw new UserDisabledException("User account disabled");
        user.getTokens().addAll(tokenRepository.getUserTokens(user.getId()));
        return buildTokens(user, remoteHost);
    }

    @Nonnull
    @Override
    public TokenBundle updateTokens(@Nonnull String token, @Nonnull String remoteAddr) {
        if (tokenBuilder.invalid(token)) {
            throw new InvalidRefreshTokenException("Invalid or expired refresh token");
        }
        @Nonnull val user = userRepository.findByUserId(tokenBuilder.getClaim(token, "userId"))
                .orElseThrow(() -> new UserNotFoundException("Refresh token owner not found"));
        if (!user.getEnabled()) throw new UserDisabledException("User account disabled");
        user.getTokens().addAll(tokenRepository.getUserTokens(user.getId()));
        return buildTokens(user, remoteAddr);
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
        @Nonnull val accessToken = tokenBuilder.buildToken(user.getId(), accessTokenExpiredIn);
        @Nonnull val refreshToken = tokenBuilder.buildToken(user.getId(), currentTime.plus(60, ChronoUnit.DAYS));
        updateRefreshToken(user, remoteHost, refreshToken);

        return new TokenBundle(accessToken, refreshToken, accessTokenExpiredIn.toEpochMilli());
    }

    private void updateRefreshToken(@Nonnull User user, @Nonnull String remoteHost, @Nonnull String refreshToken) {

//        TODO: remove to another module or simple CronTab task with python
        user.getTokens().removeIf(token -> tokenBuilder.invalid(token.getToken()));

        boolean tokenExist = user.getTokens().stream()
                .map(Token::getRemoteIp)
                .anyMatch(ip -> ip.equals(remoteHost));

        if (tokenExist) {
            tokenRepository.updateRefreshToken(user, refreshToken, remoteHost);
        } else {
            tokenRepository.saveRefreshToken(user, refreshToken, remoteHost);
        }
    }
}
