package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.domain.entity.WalletEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@Repository
public interface WalletRepository extends CrudRepository<WalletEntity, String> {
}
