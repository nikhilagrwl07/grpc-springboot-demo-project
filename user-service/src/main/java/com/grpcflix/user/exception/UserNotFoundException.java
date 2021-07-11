package com.grpcflix.user.exception;

public class UserNotFoundException extends RuntimeException {
    private String login;
    public UserNotFoundException(String login, String message) {
        super(message);
        this.login=login;
    }

    public String getLogin() {
        return login;
    }
}
