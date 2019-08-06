package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  private final WalletRepository walletRepository;

  public WalletService(final WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

}
