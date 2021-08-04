package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.WalletTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionsRepository extends JpaRepository<WalletTransactions, Long> {
  List<WalletTransactions> findByWalletId(String walletId);
}
