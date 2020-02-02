package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.model.Wallet;

public interface WalletCommandService {
	
	Wallet createWallet(Wallet wallet);
	
	void payment(Integer walletId, Double amount);
	
	void charge(Integer walletId, Double amount);

}
