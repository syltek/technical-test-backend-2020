package com.playtomic.tests.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.playtomic.tests.wallet.store.Wallet;

@Repository
public interface IWalletRepository extends JpaRepository<Wallet, Integer> {}
