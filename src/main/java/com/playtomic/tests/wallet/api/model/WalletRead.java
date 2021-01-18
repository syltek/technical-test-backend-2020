package com.playtomic.tests.wallet.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class WalletRead {

    @Schema(required = true, description = "The unique identifier of the requested wallet")
    private Long id;

    @Schema(required = true, description = "The balance in the wallet")
    private BigDecimal balance;

    @Schema(required = true, description = "The balance in the wallet")
    private String currency;


}
