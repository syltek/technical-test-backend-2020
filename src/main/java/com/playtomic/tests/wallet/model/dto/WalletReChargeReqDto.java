package com.playtomic.tests.wallet.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WalletReChargeReqDto {

    private Long id;
    private BigDecimal chargeAmount;
    private String paymentChannel;

}
