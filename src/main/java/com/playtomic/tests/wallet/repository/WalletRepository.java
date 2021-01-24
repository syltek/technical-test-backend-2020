package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.model.entity.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet,Long> {

}
