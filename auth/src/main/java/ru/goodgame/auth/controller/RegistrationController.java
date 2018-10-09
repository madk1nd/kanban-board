package ru.goodgame.auth.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.goodgame.auth.service.IRegistrationService;

import javax.annotation.Nonnull;

@CrossOrigin
@RestController
public class RegistrationController {

    @Nonnull private final IRegistrationService registrationService;

    public RegistrationController(@Nonnull IRegistrationService registrationService) {
        this.registrationService = registrationService;
    }


}
