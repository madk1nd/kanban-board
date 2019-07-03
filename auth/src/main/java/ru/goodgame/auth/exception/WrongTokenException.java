package ru.goodgame.auth.exception;

import javax.annotation.Nonnull;

public class WrongTokenException extends RuntimeException {
    public WrongTokenException(@Nonnull final String s) {
        super(s);
    }
}
