package ru.goodgame.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.goodgame.auth.service.IRegistrationService;

import javax.annotation.Nonnull;

@CrossOrigin
@RestController
public class RegistrationController {

    @Nonnull private final IRegistrationService registrationService;

    public RegistrationController(@Nonnull IRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(path = "/register", consumes = {"application/x-www-form-urlencoded"})
    public ResponseEntity register(@Nonnull String email,
                                   @Nonnull String password,
                                   @Nonnull String name) {
        registrationService.registerUser(email, password, name);
        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity check(@Nonnull @RequestParam("user") String username) {
        return new ResponseEntity<>(registrationService.checkUser(username), HttpStatus.OK);
    }

}
