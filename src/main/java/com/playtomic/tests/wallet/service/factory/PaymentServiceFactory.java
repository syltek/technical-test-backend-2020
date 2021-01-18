package com.playtomic.tests.wallet.service.factory;

import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.exception.PaymentServiceErrorCode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PaymentServiceFactory {

    private static final Logger log = LoggerFactory
        .getLogger(PaymentServiceFactory.class);

    private final Map<String, PaymentService> paymentServicesAvailable;

    @Autowired
    public PaymentServiceFactory(final List<PaymentService> paymentServices) {
        paymentServicesAvailable = paymentServices.stream()
            .filter(PaymentService::isPaymentServiceActive)
            .collect(
                Collectors.toMap(PaymentService::getPaymentPlatformName, Function.identity()));
        log.info("Loaded payment services {}", paymentServicesAvailable);

    }

    public PaymentService getPaymentService(final String paymentServiceName)
        throws PaymentServiceException {

        if (!paymentServicesAvailable.containsKey(paymentServiceName)) {
            throw new PaymentServiceException(PaymentServiceErrorCode.SERVICE_NOT_AVAILABLE);
        }

        return paymentServicesAvailable.get(paymentServiceName);
    }
}
