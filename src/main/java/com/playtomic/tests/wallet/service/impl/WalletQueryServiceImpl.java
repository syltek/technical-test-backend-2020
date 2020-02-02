package com.playtomic.tests.wallet.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletQueryService;


@Service
public class WalletQueryServiceImpl implements WalletQueryService{

	private WalletRepository repository;
	
	public WalletQueryServiceImpl(WalletRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public List<Wallet> searchAll() {
		return repository.findAll();
	}
	
	@Override
	public Wallet searchById(Integer id) {
		return repository.findById(id)
				.orElseThrow(
						() -> new WalletNotFoundException(id)
					);
	}
}
