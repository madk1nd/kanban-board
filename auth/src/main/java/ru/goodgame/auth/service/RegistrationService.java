package ru.goodgame.auth.service;

import lombok.val;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.goodgame.auth.email.IMailSender;
import ru.goodgame.auth.exception.TokenExpiredException;
import ru.goodgame.auth.exception.WrongTokenException;
import ru.goodgame.auth.repository.IUserRepository;
import ru.goodgame.auth.repository.IValidationRepository;
import ru.goodgame.auth.utils.ITokenBuilder;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.google.common.hash.Hashing.sha256;

@Service
public class RegistrationService implements IRegistrationService {

    @Nonnull private final IUserRepository userRepository;
    @Nonnull private final IValidationRepository validationRepository;
    @Nonnull private final BCryptPasswordEncoder encoder;
    @Nonnull private final IMailSender mailSender;
    @Nonnull private final ITokenBuilder tokenBuilder;

    public RegistrationService(@Nonnull final IUserRepository userRepository,
                               @Nonnull final IValidationRepository validationRepository,
                               @Nonnull final BCryptPasswordEncoder encoder,
                               @Nonnull final IMailSender mailSender,
                               @Nonnull final ITokenBuilder tokenBuilder) {
        this.userRepository = userRepository;
        this.validationRepository = validationRepository;
        this.encoder = encoder;
        this.mailSender = mailSender;
        this.tokenBuilder = tokenBuilder;
    }

    @Override
    public void registerUser(@Nonnull final String email,
                             @Nonnull final String password,
                             @Nonnull final String name) {
        CompletableFuture.runAsync(() -> {
            String encoded = encoder.encode(
                    sha256()
                            .hashString(password, StandardCharsets.UTF_8)
                            .toString()
            );
            userRepository.persistUser(email, encoded, name);
        })
                .thenRunAsync(() -> {
                    val token = generateToken();
                    validationRepository.persistToken(token, email);
                    mailSender.send(email, token);
                });
    }

    @Override
    public void confirmRegistration(@Nonnull final String token) {
        val username = validationRepository.getBy(token)
                .orElseThrow(() -> new WrongTokenException("Token not found."));

        if (tokenBuilder.invalid(token)) {
            throw new TokenExpiredException("Token expired.");
        }

        userRepository.enableUser(username);
        validationRepository.deleteBy(token);
    }

    @Override
    public boolean checkUser(@Nonnull final String username) {
        return !userRepository.findByUsername(username).isPresent();
    }

    private String generateToken() {
        val expiresIn = Instant.now().plus(1, ChronoUnit.DAYS);
        return tokenBuilder.buildToken(UUID.randomUUID(), expiresIn);
    }
}
