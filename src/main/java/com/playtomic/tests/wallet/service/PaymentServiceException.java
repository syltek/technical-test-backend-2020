package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.service.exception.PaymentServiceErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 */
@NoArgsConstructor
@Getter
public class PaymentServiceException extends Exception {

    private PaymentServiceErrorCode errorCode;

    public PaymentServiceException(final PaymentServiceErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }

    public PaymentServiceException(final PaymentServiceErrorCode errorCode,
        final String message) {
        super(errorCode.getDescription() + ": " + message);
        this.errorCode = errorCode;
    }
}
