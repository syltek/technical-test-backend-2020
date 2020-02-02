package com.playtomic.tests.wallet.exception;

public class WalletFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WalletFoundException(Integer id) {
		super("Wallet with id: " + id + " is already in the system. ");
	}
}
