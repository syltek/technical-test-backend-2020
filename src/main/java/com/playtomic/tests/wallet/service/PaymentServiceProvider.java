package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.PaymentChannelNotFoundException;
import com.playtomic.tests.wallet.model.enums.PaymentChannels;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class PaymentServiceProvider {

    private static final List<PaymentChannels> supportedChannels = Arrays.asList(
            PaymentChannels.values()
    );

    public PaymentChannels checkAndValidatePaymentChannel(String channel) {
        return supportedChannels.stream()
                .filter(paymentChannels -> paymentChannels.name().equals(channel))
                .findFirst()
                .orElseThrow(() -> new PaymentChannelNotFoundException("Channel Type not supported"));
    }


}
