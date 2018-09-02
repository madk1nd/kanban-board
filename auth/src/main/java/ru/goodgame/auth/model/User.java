package ru.goodgame.auth.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

@Data
public class User {
    private final UUID id;
    private final String username;
    private final String password;
    private final List<Token> tokens = new ArrayList<>();

    public User(Object id, String username, String password) {
        this.id = UUID.fromString(id.toString());
        this.username = username;
        this.password = password;
    }
}
