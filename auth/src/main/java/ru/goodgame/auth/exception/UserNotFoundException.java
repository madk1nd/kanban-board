package ru.goodgame.auth.exception;

import javax.annotation.Nonnull;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(@Nonnull final String s) {
        super(s);
    }
}
