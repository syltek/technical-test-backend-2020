package com.playtomic.tests.wallet.domain.dto;

import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class Transaction {

  @NotNull
  private BigDecimal amount;

  @NotNull
  private Operation operation;

  public Operation getOperation() {
    return operation;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  @SuppressWarnings("unused") // needed for jackson deserialization
  public Transaction() {
  }

  public Transaction(BigDecimal amount,
      Operation operation) {
    this.amount = amount;
    this.operation = operation;
  }
}
