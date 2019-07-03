package ru.goodgame.auth.model;

import lombok.Data;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User {
    private final UUID id;
    private final String username;
    private final String password;
    private final Boolean enabled;
    private final List<Token> tokens = new ArrayList<>();

    public User(@Nonnull final Object id,
                @Nonnull final String username,
                @Nonnull final String password,
                @Nonnull final Boolean enabled) {
        this.id = UUID.fromString(id.toString());
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }
}
