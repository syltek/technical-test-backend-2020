package com.playtomic.tests.wallet.exception;

public class WalletChargeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WalletChargeException(Integer id) {
		super("Wallet with id: " + id + " could not be charged");
	}
}
