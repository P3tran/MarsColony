package com.pefthymiou.mars.unit.domain;

public class UnprocessableRequestException extends RuntimeException {

    public UnprocessableRequestException(String message) {
        super(message);
    }
}
