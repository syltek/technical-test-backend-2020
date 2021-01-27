package com.playtomic.tests.wallet.ex;

import lombok.Getter;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */

@Getter
public class ErrorException extends RuntimeException {
    private final ErrorType errorType;
    private final String message;

    public ErrorException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
        this.message = errorType.getMessage() + message;
    }
}
