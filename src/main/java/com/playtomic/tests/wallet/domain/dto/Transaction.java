package com.playtomic.tests.wallet.domain.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class Transaction {

  @NotNull
  private Long walletId;

  @NotNull
  private BigDecimal amount;

  private Operation operation;

  public Long getWalletId() {
    return walletId;
  }

  public Operation getOperation() {
    return operation;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Transaction() {
  }

  public Transaction(Long walletId, BigDecimal amount,
      Operation operation) {
    this.walletId = walletId;
    this.amount = amount;
    this.operation = operation;
  }
}
