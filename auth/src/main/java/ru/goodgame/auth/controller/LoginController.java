package ru.goodgame.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.goodgame.auth.service.IAuthService;

import javax.annotation.Nonnull;

@RestController
public class LoginController {

    @Nonnull private final IAuthService service;

    public LoginController(@Nonnull IAuthService service) {
        this.service = service;
    }

    @PostMapping(path = "/auth/login", consumes = {"application/x-www-form-urlencoded"})
    public ResponseEntity login(@Nonnull String username, @Nonnull String password) {
        return new ResponseEntity<>(service.generateTokens(username, password), HttpStatus.OK);
    }
}
