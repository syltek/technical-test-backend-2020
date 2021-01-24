package com.playtomic.tests.wallet.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PaymentServiceException extends RuntimeException {
    public PaymentServiceException(final String message) { super(message);}
}
