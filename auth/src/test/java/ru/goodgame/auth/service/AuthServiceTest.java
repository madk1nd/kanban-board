package ru.goodgame.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.goodgame.auth.dto.TokenBundle;
import ru.goodgame.auth.exception.InvalidRefreshTokenException;
import ru.goodgame.auth.exception.NotAuthorizedException;
import ru.goodgame.auth.exception.UserNotFoundException;
import ru.goodgame.auth.model.User;
import ru.goodgame.auth.repository.ITokenRepository;
import ru.goodgame.auth.repository.IUserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
        when(user.getPassword()).thenReturn("$2a$10$M31vW1hurpGOBYOs6Z.Dk.iUuN9txYG9Oi0pDv9t36sZAzpF9MK1u");

        IUserRepository userRepository = mock(IUserRepository.class);
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        when(userRepository.findByUserId(uuid.toString())).thenReturn(Optional.of(user));

        ITokenRepository tokenRepository = mock(ITokenRepository.class);

        service = new AuthService(userRepository, tokenRepository, new BCryptPasswordEncoder());
        service.setSecret(secret);
    }

    @Test
    public void generateTokens() {
        TokenBundle actual = service.generateTokens("user1", "password", "127.0.0.1");

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
        service.generateTokens("wronUserName", "password", "127.0.0.1");
    }

    @Test(expected = NotAuthorizedException.class)
    public void generateTokens_passwordWrong() {
        service.generateTokens("user1", "wrong_password", "127.0.0.1");
    }

    @Test(expected = InvalidRefreshTokenException.class)
    public void udpateTokens_tokenInvalid() {
        service.updateTokens("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJiMDhmODZhZi0zNWRhLTQ4ZjItOGZhYi1jZWYzOTA0NjYwYmQifQ.-xN_h82PHVTCMA9vdoHrcZxH-x5mb11y1537t3rGzcM", "127.0.0.1");
    }

    @Test(expected = UserNotFoundException.class)
    public void updateTokens_userNotFound() {
        service.updateTokens(service.buildToken(UUID.randomUUID(), Instant.now().plus(5, ChronoUnit.MINUTES)), "127.0.0.1");
    }

    @Test
    public void checkPassword() {
        User user = new User(UUID.randomUUID(), "admin", "$2a$10$SY2YXk2TG9N9B0w9OTWWsua3lBSB.my/OMNJZWxIF36N3eyodOIlK");
        service.checkPassword("12345", user);
    }

    @Test
    public void updateTokens() {
        TokenBundle tokenBundle = service.generateTokens("user1", "password", "127.0.0.1");
        TokenBundle actual = service.updateTokens(tokenBundle.getRefreshToken(), "127.0.0.1");

        assertEquals(service.userIdFrom(tokenBundle.getAccessToken()), service.userIdFrom(actual.getAccessToken()));
        assertEquals(service.userIdFrom(tokenBundle.getRefreshToken()), service.userIdFrom(actual.getRefreshToken()));
    }

    @Test
    public void userIdFrom() {
        TokenBundle tokenBundle = service.generateTokens("user1", "password", "127.0.0.1");

        String idFromAccessToken = service.userIdFrom(tokenBundle.getAccessToken());
        String idFromRefreshToken =  service.userIdFrom(tokenBundle.getRefreshToken());

        assertEquals(uuid, UUID.fromString(idFromAccessToken));
        assertEquals(uuid, UUID.fromString(idFromRefreshToken));
    }


}