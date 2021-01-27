package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.domain.entity.WalletTransactionEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@Repository
public interface WalletTransactionRepository extends CrudRepository<WalletTransactionEntity, String> {

    @Query(value = "SELECT SUM(AMOUNT) FROM WALLET_TRANSACTION WHERE WALLET_ID=?1", nativeQuery = true)
    BigDecimal getBalance(String walletId);
}
