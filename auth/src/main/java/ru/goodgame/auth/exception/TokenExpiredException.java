package ru.goodgame.auth.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String s) {
        super(s);
    }
}
