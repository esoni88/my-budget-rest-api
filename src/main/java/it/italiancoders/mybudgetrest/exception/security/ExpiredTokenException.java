package it.italiancoders.mybudgetrest.exception.security;

public class ExpiredTokenException extends RuntimeException {
    public ExpiredTokenException(String s) {
        super(s);
    }
}
