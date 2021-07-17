package com.grpcflix.aggregator.exception;

public class BadRequestException extends Throwable {
    public BadRequestException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
