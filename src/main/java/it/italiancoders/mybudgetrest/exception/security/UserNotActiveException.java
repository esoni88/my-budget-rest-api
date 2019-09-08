package it.italiancoders.mybudgetrest.exception.security;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(String s) {
        super(s);
    }
}
