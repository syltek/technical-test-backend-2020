package com.playtomic.tests.wallet.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Information about the charging to be performed")
@ToString
public class WalletChargeDetails {

    @NotNull
    @Min(0)
    private BigDecimal amount;

    @NotBlank
    @Schema(implementation = SupportedCurrencies.class)
    private String currency;
}
