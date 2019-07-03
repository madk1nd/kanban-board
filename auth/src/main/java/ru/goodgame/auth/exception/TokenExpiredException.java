package ru.goodgame.auth.exception;

import javax.annotation.Nonnull;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(@Nonnull final String s) {
        super(s);
    }
}
