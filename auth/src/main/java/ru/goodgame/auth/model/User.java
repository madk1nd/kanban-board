package ru.goodgame.auth.model;

import lombok.Data;

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

    public User(Object id, String username, String password, Boolean enabled) {
        this.id = UUID.fromString(id.toString());
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }
}
