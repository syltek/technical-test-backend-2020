package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.exception.PaymentServiceErrorCode;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;


/**
 * A real implementation would call to a third party's payment service (such as Stripe, Paypal,
 * Redsys...).
 * <p>
 * This is a dummy implementation which throws an error when trying to change less than 10€.
 */
@Service("thirdPartyPaymentService")
public class ThirdPartyPaymentService implements PaymentService {

    private final BigDecimal threshold = new BigDecimal(10);

    @Override
    public void charge(final BigDecimal amount) throws PaymentServiceException {
        if (amount.compareTo(threshold) < 0) {
            throw new PaymentServiceException(PaymentServiceErrorCode.INSUFFICIENT_AMOUNT,
                "The minimum amount to charge in thirdPartyPaymentService service is " + threshold);
        }
    }

    @Override
    public boolean isPaymentServiceActive() {
        return true;
    }
}
