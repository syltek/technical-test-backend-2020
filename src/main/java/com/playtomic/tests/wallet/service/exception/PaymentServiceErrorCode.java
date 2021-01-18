package com.playtomic.tests.wallet.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentServiceErrorCode {

    SERVICE_NOT_AVAILABLE("The payment service requested is not available"),
    INSUFFICIENT_AMOUNT("Insufficient amount");

    private final String description;


}
