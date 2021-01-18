package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;

public interface PaymentService {

    void charge(BigDecimal amount) throws PaymentServiceException;

    boolean isPaymentServiceActive();

    default String getPaymentPlatformName() {
        return this.getClass().getAnnotation(Service.class).value();
    }
}
