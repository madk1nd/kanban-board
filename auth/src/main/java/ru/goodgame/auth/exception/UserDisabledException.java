package ru.goodgame.auth.exception;

import javax.annotation.Nonnull;

public class UserDisabledException extends RuntimeException {
    public UserDisabledException(@Nonnull final String s) { super(s); }
}
