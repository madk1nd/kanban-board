package ru.goodgame.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.goodgame.auth.service.IRegistrationService;

import javax.annotation.Nonnull;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class RegistrationController {

    @Nonnull private final IRegistrationService registrationService;

    public RegistrationController(@Nonnull final IRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(path = "/register", consumes = {"application/x-www-form-urlencoded"})
    public ResponseEntity register(@Nonnull final String email,
                                   @Nonnull final String password,
                                   @Nonnull final String name) {
        registrationService.registerUser(email, password, name);
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity check(@Nonnull @RequestParam("user") final String username) {
        return new ResponseEntity<>(registrationService.checkUser(username), HttpStatus.OK);
    }

}
