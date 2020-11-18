package com.playtomic.tests.wallet.dto;

import java.math.BigDecimal;

public class Payment extends Wallet {

	public Payment(String uniqueId, BigDecimal amount) {
		super.setId(uniqueId);
		super.setBalance(amount);
	}
	
	public BigDecimal getAmount() {
		return super.getBalance();
	}
}