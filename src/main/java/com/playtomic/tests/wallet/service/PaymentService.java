package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;

public interface PaymentService {
    void charge(final BigDecimal amount, final String paymentChannel) throws PaymentServiceException;
}
