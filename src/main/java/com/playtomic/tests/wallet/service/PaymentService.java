package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;

import com.playtomic.tests.wallet.exception.PaymentServiceException;

public interface PaymentService {
    void charge(BigDecimal amount) throws PaymentServiceException;
}
