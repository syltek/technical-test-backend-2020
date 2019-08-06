package com.playtomic.tests.wallet.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
  private PaymentService paymentService = mock(PaymentService.class);
  private WalletService walletService = new WalletService(this.walletRepository, this.paymentService);

  @Test
  public void updateWalletBalanceNoWallet() throws PaymentServiceException {
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.empty());
    final Transaction transaction = new Transaction(WALLET_ID, AMOUNT, Operation.PAYMENT);
    WalletServiceException exception = null;
    try {
      this.walletService.updateWalletBalance(transaction);
    } catch (WalletServiceException e) {
      exception = e;
    }
    verify(this.paymentService, never()).charge(any(BigDecimal.class));
    assertNotNull(exception);
    assertEquals(exception.getCode(), 404);
    assertEquals(exception.getMessage(), "Wallet with id: " + WALLET_ID + " was not found");
  }

  @Test
  public void updateWalletBalancePayment() throws PaymentServiceException {
    final Wallet wallet = new Wallet();
    wallet.setBalance(BALANCE);
    wallet.setId(WALLET_ID);
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
    final Transaction transaction = new Transaction(WALLET_ID, AMOUNT,
        Operation.PAYMENT);
    this.walletService.updateWalletBalance(transaction);
    verify(this.paymentService, never()).charge(any(BigDecimal.class));
    assertEquals(wallet.getBalance(), BigDecimal.valueOf(-50.0));
  }

  @Test
  public void updateWalletBalanceRefund() throws PaymentServiceException {
    final Wallet wallet = new Wallet();
    wallet.setBalance(BALANCE);
    wallet.setId(WALLET_ID);
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
    final Transaction transaction = new Transaction(WALLET_ID, AMOUNT, Operation.REFUND);
    this.walletService.updateWalletBalance(transaction);
    verify(this.paymentService, never()).charge(any(BigDecimal.class));
    assertEquals(wallet.getBalance(), BigDecimal.valueOf(150.0));
  }

  @Test
  public void updateWalletBalanceRecharge() throws PaymentServiceException {
    final Wallet wallet = new Wallet();
    wallet.setBalance(BALANCE);
    wallet.setId(WALLET_ID);
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
    final Transaction transaction = new Transaction(WALLET_ID, AMOUNT, Operation.RECHARGE);
    this.walletService.updateWalletBalance(transaction);
    verify(this.paymentService, times(1)).charge(eq(AMOUNT));
    assertEquals(wallet.getBalance(), BigDecimal.valueOf(150.0));
  }

  @Test
  public void updateWalletBalanceRechargeThrowsPaymentException() throws PaymentServiceException {
    final Wallet wallet = new Wallet();
    wallet.setBalance(BALANCE);
    wallet.setId(WALLET_ID);
    when(this.walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));
    doThrow(new PaymentServiceException()).when(this.paymentService).charge(eq(AMOUNT));
    final Transaction transaction = new Transaction(WALLET_ID, AMOUNT, Operation.RECHARGE);
    PaymentServiceException exception = null;
    try {
      this.walletService.updateWalletBalance(transaction);
    } catch (PaymentServiceException e) {
      exception = e;
    }
    assertNotNull(exception);
  }

}