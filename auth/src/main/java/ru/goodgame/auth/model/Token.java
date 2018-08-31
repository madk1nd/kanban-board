package ru.goodgame.auth.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Token {
    private final UUID id;
    private final UUID userId;
    private final String token;
    private final String remoteIp;

    public Token(Object id, Object userId, String token, String remoteIp) {
        this.id = UUID.fromString(id.toString());
        this.userId = UUID.fromString(userId.toString());
        this.token = token;
        this.remoteIp = remoteIp;
    }
}
