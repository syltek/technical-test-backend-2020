package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.model.enums.PaymentChannels;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.PaymentServiceProvider;
import org.junit.Test;

import java.math.BigDecimal;

public class ThirdPartyPaymentServiceTest {

    PaymentServiceProvider provider = new PaymentServiceProvider();
    ThirdPartyPaymentService s = new ThirdPartyPaymentService(provider);

    @Test(expected = PaymentServiceException.class)
    public void test_exception() throws PaymentServiceException {
        s.charge(new BigDecimal(5), "AMAZON");
    }

    @Test
    public void test_itWorks_asExpected() {
        s.charge(new BigDecimal(11), PaymentChannels.PAYPAL.name());
    }
}
