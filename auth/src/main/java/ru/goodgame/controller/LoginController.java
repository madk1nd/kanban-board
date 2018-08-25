package ru.goodgame.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Value("${jwt.secret}")
    private String secret;

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
}
