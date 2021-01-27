package com.playtomic.tests.wallet.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class WalletDto {
    @JsonProperty(value = "walletId")
    private String walletId;
    @JsonProperty(value = "description")
    private String description;
    @JsonProperty(value = "balance")
    private BigDecimal balance;
}
