package com.playtomic.tests.wallet.service;

import java.util.List;

import com.playtomic.tests.wallet.model.Wallet;

public interface WalletQueryService {

	List<Wallet> searchAll();
	
	Wallet searchById(Integer id);

}
