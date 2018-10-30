package ru.goodgame.auth.exception;

public class UserDisabledException extends RuntimeException {
    public UserDisabledException(String s) { super(s); }
}
