package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.service.PaymentServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ThirdPartyPaymentServiceTest {

    final ThirdPartyPaymentService s = new ThirdPartyPaymentService();

    @Test
    public void test_exception() {
        Executable executable = () -> s.charge(new BigDecimal(5));
        assertThrows(PaymentServiceException.class, executable);
    }

    @Test
    public void test_ok() {
        Executable executable = () -> s.charge(new BigDecimal(15));
        assertDoesNotThrow(executable);
    }
}
