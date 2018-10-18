package ru.goodgame.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.goodgame.auth.service.IAuthService;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class LoginController {

    @Nonnull private final IAuthService service;

    public LoginController(@Nonnull IAuthService service) {
        this.service = service;
    }

    @PostMapping(path = "/login", consumes = {"application/x-www-form-urlencoded"})
    public ResponseEntity login(@Nonnull String username,
                                @Nonnull String password,
                                HttpServletRequest request) {
        return new ResponseEntity<>(
                service.generateTokens(username, password, request.getRemoteAddr()),
                HttpStatus.OK
        );
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity refresh(@RequestBody String token, HttpServletRequest request) {
        return new ResponseEntity<>(
                service.updateTokens(token, request.getRemoteAddr()),
                HttpStatus.OK
        );
    }
}
