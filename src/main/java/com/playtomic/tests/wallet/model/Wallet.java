package com.playtomic.tests.wallet.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Wallet {

    @Id
    private Long id;
    private BigDecimal balance;
    private String currency;
}
