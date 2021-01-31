package com.playtomic.tests.wallet.domain.entity;

import com.playtomic.tests.wallet.domain.enums.Currency;
import com.playtomic.tests.wallet.domain.enums.PaymentPlatform;
import com.playtomic.tests.wallet.domain.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallet_transaction")
public class WalletTransactionEntity {

    @Id
    @Column(name = "transaction_id")
    private String id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "payment_platform")
    @Enumerated(EnumType.STRING)
    private PaymentPlatform paymentPlatform;

    @ManyToOne
    @JoinColumn(name = "wallet_id", foreignKey = @ForeignKey(name = "fk_wallet"), updatable = false)
    private WalletEntity walletEntity;

    @Column(name = "created")
    private Timestamp created;

}
