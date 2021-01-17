package com.playtomic.tests.wallet.repositories;

import com.playtomic.tests.wallet.data.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet,Long> {

}
