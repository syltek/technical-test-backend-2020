package com.playtomic.tests.wallet.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class WithdrawDto {
    @JsonProperty(value = "walletId")
    private String walletId;
    @JsonProperty(value = "amount")
    private BigDecimal amount;
}
