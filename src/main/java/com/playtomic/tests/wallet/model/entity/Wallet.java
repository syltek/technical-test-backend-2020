package com.playtomic.tests.wallet.model.entity;

import com.playtomic.tests.wallet.model.enums.CurrencyTypes;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "WALLET")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Wallet extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CurrencyTypes currency;

    @NotNull
    @DecimalMin(value = "0.00", message = "balance can not be less than 0.00")
    private BigDecimal balance;
}
