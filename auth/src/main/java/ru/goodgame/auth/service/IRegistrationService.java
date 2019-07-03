package ru.goodgame.auth.service;

import javax.annotation.Nonnull;

public interface IRegistrationService {
    void registerUser(@Nonnull String email,
                      @Nonnull String password,
                      @Nonnull String name);
    void confirmRegistration(@Nonnull String token);
    boolean checkUser(@Nonnull String username);
}
