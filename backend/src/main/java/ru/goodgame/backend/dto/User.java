package ru.goodgame.backend.dto;

import lombok.Data;

import javax.annotation.Nonnull;

@Data
public class User {
    @Nonnull private final String uuid;
    @Nonnull private final String username;
    @Nonnull private final String password;
}
