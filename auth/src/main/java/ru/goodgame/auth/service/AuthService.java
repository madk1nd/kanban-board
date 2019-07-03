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
import java.util.List;

import static com.google.common.hash.Hashing.sha256;

@Service
@Slf4j
public class AuthService implements IAuthService {

    @Nonnull private final IUserRepository userRepository;
    @Nonnull private final ITokenRepository tokenRepository;
    @Nonnull private final BCryptPasswordEncoder encoder;
    @Nonnull private final ITokenBuilder tokenBuilder;

    public AuthService(@Nonnull final IUserRepository userRepository,
                       @Nonnull final ITokenRepository tokenRepository,
                       @Nonnull final BCryptPasswordEncoder encoder,
                       @Nonnull final ITokenBuilder tokenBuilder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
        this.tokenBuilder = tokenBuilder;
    }

    @Override
    @Nonnull
    public TokenBundle generateTokens(@Nonnull final String username,
                                      @Nonnull final String password,
                                      @Nonnull final String remoteHost) {
        val user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with username=%s not found", username)
                ));

        checkPassword(password, user);
        if (!user.getEnabled()) {
            throw new UserDisabledException("User account disabled");
        }

        val userTokens = tokenRepository.getUserTokens(user.getId());
        user.getTokens().addAll(userTokens);

        return buildTokens(user, remoteHost);
    }

    @Nonnull
    @Override
    public TokenBundle updateTokens(@Nonnull final String token,
                                    @Nonnull final String remoteAddr) {
        if (tokenBuilder.invalid(token)) {
            throw new InvalidRefreshTokenException("Invalid or expired refresh token");
        }

        val user = userRepository.findByUserId(
                tokenBuilder.getClaim(token, "userId")
        )
                .orElseThrow(() -> new UserNotFoundException("Refresh token owner not found"));

        if (!user.getEnabled()) {
            throw new UserDisabledException("User account disabled");
        }

        val userTokens = tokenRepository.getUserTokens(user.getId());
        user.getTokens().addAll(userTokens);

        return buildTokens(user, remoteAddr);
    }

    void checkPassword(@Nonnull final String password,
                       @Nonnull final User user) {
        val hash = sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        if (!encoder.matches(hash, user.getPassword())) {
            throw new NotAuthorizedException("invalid password");
        }
    }

    @Nonnull
    private TokenBundle buildTokens(@Nonnull final User user,
                                    @Nonnull final String remoteHost) {
        val currentTime = Instant.now();

        val accessTokenExpiredIn = currentTime.plus(30, ChronoUnit.MINUTES);
        val accessToken = tokenBuilder.buildToken(
                user.getId(),
                accessTokenExpiredIn
        );
        val refreshToken = tokenBuilder.buildToken(
                user.getId(),
                currentTime.plus(60, ChronoUnit.DAYS)
        );
        updateRefreshToken(user, remoteHost, refreshToken);

        return new TokenBundle(
                accessToken,
                refreshToken,
                accessTokenExpiredIn.toEpochMilli()
        );
    }

    private void updateRefreshToken(@Nonnull final User user,
                                    @Nonnull final String remoteHost,
                                    @Nonnull final String refreshToken) {

//        TODO: remove to another module or simple CronTab task with python
        user.getTokens().removeIf(token -> tokenBuilder.invalid(token.getToken()));

        val tokenExist = user.getTokens().stream()
                .map(Token::getRemoteIp)
                .anyMatch(ip -> ip.equals(remoteHost));

        if (tokenExist) {
            tokenRepository.updateRefreshToken(user, refreshToken, remoteHost);
        } else {
            tokenRepository.saveRefreshToken(user, refreshToken, remoteHost);
        }
    }
}
