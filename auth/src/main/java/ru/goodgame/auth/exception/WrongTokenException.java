package ru.goodgame.auth.exception;

public class WrongTokenException extends RuntimeException {
    public WrongTokenException(String s) {
        super(s);
    }
}
