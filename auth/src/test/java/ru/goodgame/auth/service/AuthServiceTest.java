package ru.goodgame.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.goodgame.auth.dto.TokenBundle;
import ru.goodgame.auth.exception.NotAuthorizedException;
import ru.goodgame.auth.exception.UserNotFoundException;
import ru.goodgame.auth.model.User;
import ru.goodgame.auth.repository.ITokenRepository;
import ru.goodgame.auth.repository.IUserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthServiceTest {

    private static final String secret = "468AB6CF578FCCCFE8852151C0C3C5165B5B2EBAA9EE43454040F9621129B7220028FC88F329881CAF4C30E9700340FB22DE57AD076DA37B804C4AC95981B677";

    private AuthService service;
    private UUID uuid;

    @Before
    public void init() {
        uuid = UUID.randomUUID();

        User user = mock(User.class);
        when(user.getId()).thenReturn(uuid);
        when(user.getUsername()).thenReturn("user1");
        when(user.getPassword()).thenReturn("$2a$10$Kn60f33G7LyYShz/QEHJB.AMkY.rqg79CbrzPBz4YryaEEcv/h8TK");

        IUserRepository userRepository = mock(IUserRepository.class);
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        ITokenRepository tokenRepository = mock(ITokenRepository.class);

        service = new AuthService(userRepository, tokenRepository, new BCryptPasswordEncoder());
        service.setSecret(secret);
    }

    @Test
    public void generateTokens() {
        TokenBundle actual = service.generateTokens("user1", "password");

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(actual.getAccessToken());
        Object userId = claims.getBody().get("userId");
        String expiresIn = claims.getBody().get("expiresIn").toString();

        Jws<Claims> claims2 = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(actual.getRefreshToken());
        Object userId2 = claims2.getBody().get("userId");
        String expiresIn2 = claims2.getBody().get("expiresIn").toString();

        assertNotNull(actual);

        assertEquals(uuid.toString(), userId);
        assertNotNull(expiresIn);
        assertEquals(Long.parseLong(expiresIn), actual.getAccessTokenExpiredIn());

        assertEquals(uuid.toString(), userId2);
        assertNotNull(expiresIn2);
    }

    @Test(expected = UserNotFoundException.class)
    public void generateTokens_userNotFound() {
        service.generateTokens("wronUserName", "password");
    }

    @Test(expected = NotAuthorizedException.class)
    public void generateTokens_passwordWrong() {
        service.generateTokens("user1", "wrong_password");
    }
}