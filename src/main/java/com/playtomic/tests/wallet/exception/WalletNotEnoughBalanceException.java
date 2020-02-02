package com.playtomic.tests.wallet.exception;

public class WalletNotEnoughBalanceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WalletNotEnoughBalanceException(Integer id) {
		super("Wallet with id: " + id + " has not enough balance to do the payment ");
	}
}
