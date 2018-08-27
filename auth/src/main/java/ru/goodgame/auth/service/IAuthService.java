package ru.goodgame.auth.service;

import ru.goodgame.auth.dto.TokenBundle;

import javax.annotation.Nonnull;

public interface IAuthService {
    @Nonnull TokenBundle generateTokens(@Nonnull String username, @Nonnull String password);
}
