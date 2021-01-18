package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.service.PaymentServiceException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class ThirdPartyPaymentServiceTest {

    ThirdPartyPaymentService s = new ThirdPartyPaymentService();

    @Test
    public void test_exception() throws PaymentServiceException {
        Assertions.assertThrows(PaymentServiceException.class, () -> s.charge(new BigDecimal(5)));
    }

    @Test
    public void test_ok() throws PaymentServiceException {
        s.charge(new BigDecimal(15));
    }
}
