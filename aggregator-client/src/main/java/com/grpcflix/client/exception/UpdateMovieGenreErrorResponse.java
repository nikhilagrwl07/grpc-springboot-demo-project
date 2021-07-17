package com.grpcflix.client.exception;

import org.springframework.web.reactive.function.client.WebClientResponseException;

public class UpdateMovieGenreErrorResponse extends RuntimeException {
    public UpdateMovieGenreErrorResponse(String statusText, WebClientResponseException ex) {
        super(statusText, ex);
    }

    public UpdateMovieGenreErrorResponse(Exception ex) {
        super(ex);
    }
}
