package com.grpcflix.client.exception;

import org.springframework.web.reactive.function.client.WebClientResponseException;

public class RecommendedMovieErrorResponse extends RuntimeException {
    public RecommendedMovieErrorResponse(String statusText, WebClientResponseException ex) {
        super(statusText,ex);
    }

    public RecommendedMovieErrorResponse(Exception ex) {
        super(ex);
    }
}
