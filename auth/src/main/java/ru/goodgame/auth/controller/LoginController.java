package ru.goodgame.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.goodgame.auth.service.IAuthService;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Nonnull private final IAuthService service;

    public LoginController(@Nonnull IAuthService service) {
        this.service = service;
    }

    @PostMapping(path = "/auth/login", consumes = {"application/x-www-form-urlencoded"})
    public ResponseEntity login(@Nonnull String username,
                                @Nonnull String password,
                                HttpServletRequest request) {
        return new ResponseEntity<>(
                service.generateTokens(username, password, request.getRemoteAddr()),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/auth/refresh")
    public ResponseEntity login(@RequestBody String token, HttpServletRequest request) {
        return new ResponseEntity<>(
                service.updateTokens(token, request.getRemoteAddr()),
                HttpStatus.OK
        );
    }
}
