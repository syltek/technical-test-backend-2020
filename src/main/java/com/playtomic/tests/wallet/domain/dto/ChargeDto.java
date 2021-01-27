package com.playtomic.tests.wallet.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.tests.wallet.domain.enums.PaymentPlatform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChargeDto {
    @JsonProperty(value = "walletId")
    private String walletId;
    @JsonProperty(value = "paymentPlatform")
    private PaymentPlatform paymentPlatform;
    @JsonProperty(value = "amount")
    private BigDecimal amount;
}
