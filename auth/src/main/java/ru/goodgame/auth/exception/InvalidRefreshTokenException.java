package ru.goodgame.auth.exception;

import javax.annotation.Nonnull;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(@Nonnull final String s) {
        super(s);
    }
}
