package com.playtomic.tests.wallet.service;

import java.math.BigDecimal;
import java.util.List;

import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.exception.PaymentException;
import com.playtomic.tests.wallet.exception.PaymentServiceException;
import com.playtomic.tests.wallet.exception.WalletAlreadyExistsException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;

public interface WalletService {
    
	List<Wallet> findAll();
	
	Wallet findById(String id) throws WalletNotFoundException;

	Wallet rechargeWallet(String uniqueId, BigDecimal bigDecimal) throws WalletNotFoundException, PaymentServiceException;

	Wallet makePayment(String uniqueId, BigDecimal bigDecimal) throws WalletNotFoundException, PaymentException;

	Wallet saveWallet(Wallet wallet) throws WalletAlreadyExistsException;
    
}
