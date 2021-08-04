package com.playtomic.tests.wallet.model;

import java.math.BigDecimal;

public class Payment extends Wallet {
  BigDecimal amount;

  Payment(String id, BigDecimal amount) {
    super(id, amount);
    this.amount = amount;
  }
}
