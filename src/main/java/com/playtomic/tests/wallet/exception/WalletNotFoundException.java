package com.playtomic.tests.wallet.exception;

public class WalletNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WalletNotFoundException(Integer id) {
		super("Wallet with id: " + id + " not found. Please provide a valid wallet id. ");
	}
}
