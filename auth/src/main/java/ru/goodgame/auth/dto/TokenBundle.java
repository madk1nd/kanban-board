package ru.goodgame.auth.dto;

import lombok.Data;

@Data
public class TokenBundle {
    private final String accessToken;
    private final String refreshToken;
    private final long accessTokenExpiredIn;
}
