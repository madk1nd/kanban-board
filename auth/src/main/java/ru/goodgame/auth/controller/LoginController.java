package ru.goodgame.auth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.goodgame.auth.service.IAuthService;

import javax.annotation.Nonnull;

@RestController
public class LoginController {


    @Value("${jwt.secret}")
    private String secret;

    @Nonnull private final IAuthService service;

    public LoginController(@Nonnull IAuthService service) {
        this.service = service;
    }

    @GetMapping("/gwt")
    public String getGwt() {
        System.out.println(secret);
        Claims claims = Jwts.claims().setSubject("test");
        claims.put("userId", 321 + "");
        claims.put("role", "NEW");

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    @PostMapping(path = "/auth/login", consumes = {"application/x-www-form-urlencoded"})
    public String login(String username, String password) {
        System.out.println(password);
        System.out.println(username);
//        System.out.println(map);
        service.getUser();
        return username + " " + password;
    }
}
