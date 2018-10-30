package ru.goodgame.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.goodgame.auth.service.IRegistrationService;

import javax.annotation.Nonnull;

@Controller
public class ConfirmationController {

    @Nonnull private final IRegistrationService registrationService;

    public ConfirmationController(@Nonnull IRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/auth/confirm")
    public String greeting(@RequestParam("token") String token, Model model) {
        try {
            registrationService.confirmRegistration(token);
            model.addAttribute("success", "Your registration is confirmed!");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "confirm";
    }
}
