package com.playtomic.tests.wallet.service.impl;


import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.playtomic.tests.wallet.service.PaymentServiceException;

public class ThirdPartyPaymentServiceTest {

    ThirdPartyPaymentService s = new ThirdPartyPaymentService();

    @Test
    public void test_exception() throws PaymentServiceException {
        assertThrows(PaymentServiceException.class, () -> {s.charge(new BigDecimal(5));});
    }

    @Test
    public void test_ok() throws PaymentServiceException {
        s.charge(new BigDecimal(15));
    }
}
