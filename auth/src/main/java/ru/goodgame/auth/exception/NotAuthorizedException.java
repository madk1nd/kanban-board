package ru.goodgame.auth.exception;

import javax.annotation.Nonnull;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(@Nonnull final String s) {
        super(s);
    }
}
