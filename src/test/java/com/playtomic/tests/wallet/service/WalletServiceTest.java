package com.playtomic.tests.wallet.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.dto.Operation;
import com.playtomic.tests.wallet.domain.dto.Transaction;
import com.playtomic.tests.wallet.repository.WalletRepository;
import java.util.Optional;
import org.junit.Test;

public class WalletServiceTest {

  private static final Long WALLET_ID = 1L;

  private WalletRepository walletRepository = mock(WalletRepository.class);
  private WalletService walletService = new WalletService(this.walletRepository);

  @Test
  public void updateWalletBalanceNoWallet() {
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.empty());
    final Transaction transaction = new Transaction(WALLET_ID, 100.0, Operation.PAYMENT);
    WalletServiceException exception = null;
    try {
      this.walletService.updateWalletBalance(transaction);
    } catch (WalletServiceException e) {
      exception = e;
    }
    assertNotNull(exception);
    assertEquals(exception.getCode(), 404);
    assertEquals(exception.getMessage(), "Wallet with id: " + WALLET_ID + " was not found");
  }

  @Test
  public void updateWalletBalancePayment() {
    final Wallet wallet = new Wallet();
    wallet.setBalance(50.0);
    wallet.setId(WALLET_ID);
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
    final Transaction transaction = new Transaction(WALLET_ID, 100.0, Operation.PAYMENT);
    this.walletService.updateWalletBalance(transaction);
    assertEquals(wallet.getBalance(), -50.0, 0.01);
  }

  @Test
  public void updateWalletBalanceRefund() {
    final Wallet wallet = new Wallet();
    wallet.setBalance(50.0);
    wallet.setId(WALLET_ID);
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
    final Transaction transaction = new Transaction(WALLET_ID, 100.0, Operation.REFUND);
    this.walletService.updateWalletBalance(transaction);
    assertEquals(wallet.getBalance(), 150.0, 0.01);
  }

}