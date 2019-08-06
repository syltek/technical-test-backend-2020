package com.playtomic.tests.wallet.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.dto.Operation;
import com.playtomic.tests.wallet.domain.dto.Transaction;
import com.playtomic.tests.wallet.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.Test;

public class WalletServiceTest {

  private static final Long WALLET_ID = 1L;
  private static BigDecimal AMOUNT = BigDecimal.valueOf(100.0);
  private static BigDecimal BALANCE = BigDecimal.valueOf(50);

  private WalletRepository walletRepository = mock(WalletRepository.class);
  private WalletService walletService = new WalletService(this.walletRepository);

  @Test
  public void updateWalletBalanceNoWallet() {
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.empty());
    final Transaction transaction = new Transaction(WALLET_ID, AMOUNT, Operation.PAYMENT);
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
    wallet.setBalance(BALANCE);
    wallet.setId(WALLET_ID);
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
    final Transaction transaction = new Transaction(WALLET_ID, AMOUNT,
        Operation.PAYMENT);
    this.walletService.updateWalletBalance(transaction);
    assertEquals(wallet.getBalance(), BigDecimal.valueOf(-50.0));
  }

  @Test
  public void updateWalletBalanceRefund() {
    final Wallet wallet = new Wallet();
    wallet.setBalance(BALANCE);
    wallet.setId(WALLET_ID);
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
    final Transaction transaction = new Transaction(WALLET_ID, AMOUNT, Operation.REFUND);
    this.walletService.updateWalletBalance(transaction);
    assertEquals(wallet.getBalance(), BigDecimal.valueOf(150.0));
  }

  @Test
  public void updateWalletBalanceRecharge() {
    final Wallet wallet = new Wallet();
    wallet.setBalance(BALANCE);
    wallet.setId(WALLET_ID);
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
    final Transaction transaction = new Transaction(WALLET_ID, AMOUNT, Operation.REFUND);
    this.walletService.updateWalletBalance(transaction);
    assertEquals(wallet.getBalance(), BigDecimal.valueOf(150.0));
  }

}