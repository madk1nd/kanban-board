package ru.goodgame.auth.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Token {
    private final UUID id;
    private final UUID userId;
    private final String token;
    private final String remoteIp;
}
