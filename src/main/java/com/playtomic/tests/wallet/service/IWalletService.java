package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;
import java.util.List;

import com.playtomic.tests.wallet.store.Wallet;

public interface IWalletService {
	
	public Wallet findById(int id);

	public List<Wallet> getWallets();

	public Wallet save(Wallet wallet);

	public void delete(int id);

	public boolean existsWallet(int id);
	
	public Wallet chargeWallet(Wallet wallet, BigDecimal amount);
	
	public Wallet paymentWallet(Wallet wallet, BigDecimal amount);
	
	public Wallet refundWallet(Wallet wallet, BigDecimal amount);
	
	public Wallet attControl(Wallet w, Wallet w2);
	
}
