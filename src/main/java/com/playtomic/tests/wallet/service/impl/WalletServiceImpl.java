package com.playtomic.tests.wallet.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.exception.PaymentException;
import com.playtomic.tests.wallet.exception.PaymentServiceException;
import com.playtomic.tests.wallet.exception.WalletAlreadyExistsException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.WalletDAO;
import com.playtomic.tests.wallet.model.WalletMapper;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {
	
	private Logger log = LoggerFactory.getLogger(WalletController.class);
	
	@Autowired
	private WalletRepository repo;
	
	@Autowired
	private ThirdPartyPaymentService thirdPartyPaymentService;
	
	@Autowired 
	private WalletMapper mapper;

	@Override
	public List<Wallet> findAll() {
		return mapper.convertAllToDTO(repo.findAll());
	}
	
	@Override
	public Wallet findById(String id) throws WalletNotFoundException {
		Optional<WalletDAO> wallet = repo.findById(id);
		
		if(!wallet.isPresent()) {
			throw new WalletNotFoundException();
		}
		
		return mapper.convertToDTO(wallet.get());
	}
	
	@Override
	public Wallet saveWallet(Wallet wallet) throws WalletAlreadyExistsException {
		Optional<WalletDAO> walletVerify = repo.findById(wallet.getId());
		
		if(walletVerify.isPresent()) {
			throw new WalletAlreadyExistsException();
		}
		
		WalletDAO savedWallet = repo.save(mapper.convertFromDTO(wallet));
		log.info("Saved wallet id: " + savedWallet.getId() + " with balance of " + savedWallet.getBalance() + "€");
		
		return mapper.convertToDTO(savedWallet);
	}


	@Override
	public Wallet rechargeWallet(String uniqueId, BigDecimal bigDecimal) throws WalletNotFoundException, PaymentServiceException {
		Wallet wallet = this.findById(uniqueId);
		thirdPartyPaymentService.charge(bigDecimal);
		
		wallet.setBalance(wallet.getBalance().add(bigDecimal));
		
		WalletDAO savedWallet = repo.save(mapper.convertFromDTO(wallet));
		log.info("Recharged wallet id: " + savedWallet.getId() + " with " + bigDecimal + "€. Actual balance: " + savedWallet.getBalance() + "€");
		
		return mapper.convertToDTO(savedWallet);
	}

	@Override
	public Wallet makePayment(String uniqueId, BigDecimal bigDecimal) throws WalletNotFoundException, PaymentException {
		Wallet wallet = this.findById(uniqueId);
		
		if(wallet.getBalance().compareTo(bigDecimal) == -1) {
			throw new PaymentException();
		}
		
		wallet.setBalance(wallet.getBalance().subtract(bigDecimal));
		
		WalletDAO savedWallet = repo.save(mapper.convertFromDTO(wallet));
		log.info("Payment made with wallet id: " + savedWallet.getId() + " for " + bigDecimal + "€. Actual balance: " + savedWallet.getBalance() + "€");
		
		return mapper.convertToDTO(savedWallet);
	}
}
