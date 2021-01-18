package com.playtomic.tests.wallet.model.repository;

import com.playtomic.tests.wallet.model.Wallet;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface WalletRepository extends ReactiveCrudRepository<Wallet, Long> {

}
