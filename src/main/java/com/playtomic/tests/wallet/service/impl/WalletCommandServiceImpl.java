package com.playtomic.tests.wallet.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.playtomic.tests.wallet.exception.WalletChargeException;
import com.playtomic.tests.wallet.exception.WalletFoundException;
import com.playtomic.tests.wallet.exception.WalletNotEnoughBalanceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.WalletCommandService;


@Service
public class WalletCommandServiceImpl implements WalletCommandService{

	private WalletRepository repository;
	
	private PaymentService paymentService;
	
	public WalletCommandServiceImpl(WalletRepository repository, PaymentService paymentService) {
		this.repository = repository;
		this.paymentService = paymentService;
	}
	
	@Override
	public Wallet createWallet(Wallet wallet) {
		if(!isNewWallet(wallet)) {
			repository
				.findById(wallet.getId())
				.ifPresent( (u) -> {throw new WalletFoundException(u.getId());} );
		}
		return repository.save(wallet);
	}

	@Override
	public void payment(Integer walletId, Double amount) {
		Wallet wallet = repository
			.findById(walletId)
			.orElseThrow( () -> new WalletNotFoundException(walletId) );
		
		if(checkEnoughBalanceForPayment(wallet.getBalance(), amount)) {
			wallet.setBalance(wallet.getBalance() - amount);
			repository.save(wallet);
		} else {
			throw new WalletNotEnoughBalanceException(wallet.getId());
		}
	}

	@Override
	public void charge(Integer walletId, Double amount) {
		Wallet wallet = repository
				.findById(walletId)
				.orElseThrow( () -> new WalletNotFoundException(walletId) );
			
		try {
			paymentService.charge(new BigDecimal(amount));
			wallet.setBalance(wallet.getBalance() + amount);
			repository.save(wallet);
		} catch (PaymentServiceException e) {
			throw new WalletChargeException(wallet.getId());
		}
	}
	
	private boolean isNewWallet(Wallet wallet) {
		return wallet.getId() == null;
	}
	
	private boolean checkEnoughBalanceForPayment(double actualBalance, double amountToPay) {
		return actualBalance - amountToPay >= 0;
	}
}
