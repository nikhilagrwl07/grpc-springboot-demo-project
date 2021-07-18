package com.grpcflix.aggregator.exception;

public class NoMovieFoundException extends RuntimeException {
    public NoMovieFoundException(String msg) {
        super(msg);
    }
}
