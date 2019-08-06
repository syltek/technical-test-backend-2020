package com.playtomic.tests.wallet.domain.dto;

import javax.validation.constraints.NotNull;

public class Transaction {

  @NotNull
  private Long walletId;

  @NotNull
  private Double amount;

  private Operation operation;

  public Long getWalletId() {
    return walletId;
  }

  public Operation getOperation() {
    return operation;
  }

  public Double getAmount() {
    return amount;
  }

  public Transaction() {
  }

  public Transaction(Long walletId, Double amount,
      Operation operation) {
    this.walletId = walletId;
    this.amount = amount;
    this.operation = operation;
  }
}
