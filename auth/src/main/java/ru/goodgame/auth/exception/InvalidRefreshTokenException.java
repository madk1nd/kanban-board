package ru.goodgame.auth.exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(String s) {
        super(s);
    }
}
