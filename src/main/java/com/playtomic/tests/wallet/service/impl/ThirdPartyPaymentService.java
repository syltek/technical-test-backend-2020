package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.model.enums.PaymentChannels;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.PaymentService;

import com.playtomic.tests.wallet.service.PaymentServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


/**
 * A real implementation would call to a third party's payment service (such as Stripe, Paypal, Redsys...).
 *
 * This is a dummy implementation which throws an error when trying to change less than 10€.
 */
@Service
public class ThirdPartyPaymentService implements PaymentService {
    private Logger log = LoggerFactory.getLogger(ThirdPartyPaymentService.class);

    private BigDecimal threshold = new BigDecimal(10);

    private PaymentServiceProvider paymentServiceProvider;

    public ThirdPartyPaymentService(PaymentServiceProvider paymentServiceProvider) {
        this.paymentServiceProvider = paymentServiceProvider;
    }

    @Override
    public void charge(final BigDecimal amount, final String paymentChannel) throws PaymentServiceException {
        if (amount.compareTo(threshold) < 0) {
            throw new PaymentServiceException("Payment channel not acceptable");
        }

        PaymentChannels paymentChannels = paymentServiceProvider.checkAndValidatePaymentChannel(paymentChannel);
        retrieveBalanceInfoFromThirdParty(paymentChannels);
    }

    private void retrieveBalanceInfoFromThirdParty (PaymentChannels channel) {
        log.info("Provided channel -> {}", channel.name());

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
