package com.playtomic.tests.wallet.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.playtomic.tests.wallet.repository.IWalletRepository;
import com.playtomic.tests.wallet.service.IWalletService;
import com.playtomic.tests.wallet.store.Wallet;
import com.playtomic.tests.wallet.store.WalletMovement;
import com.playtomic.tests.wallet.utils.Constants;

@Service
public class WalletServiceImpl implements IWalletService {
		
	@Autowired
	private IWalletRepository walletRepository;
	
	
	public List<Wallet> getWallets() {
		return (List<Wallet>) walletRepository.findAll();
	}
	
	public Wallet findById(int id) {
        return walletRepository.findOne(id);
    }
	
    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }
	
    public void delete(int id) {
		walletRepository.delete(id);
    }
	
    public boolean existsWallet(int id) {
        return walletRepository.exists(id);
    }

	@Override
	public Wallet paymentWallet(Wallet wallet, BigDecimal amount) {
		wallet.addMovement(Constants.PAYMENT, amount);
		wallet.setBalance(wallet.getBalance().subtract(amount));
		return walletRepository.save(wallet);
	}

	@Override
	public Wallet refundWallet(Wallet wallet, BigDecimal amount) {
		wallet.addMovement(Constants.REFOUND, amount);
		wallet.setBalance(wallet.getBalance().add(amount));
		return walletRepository.save(wallet);
	}

	@Override
	public Wallet chargeWallet(Wallet wallet, BigDecimal amount) {
		wallet.addMovement(Constants.CHARGE, amount);
		wallet.setBalance(wallet.getBalance().add(amount));
		return walletRepository.save(wallet);
	}
	
	public Wallet attControl(Wallet w, Wallet w2) {
		Wallet wCobine = null;
		if(w.getBalance()==null) {
			w.setBalance(w2.getBalance());
		}
		if(w.getName()==null) {
			w.setName(w2.getName());
		}
		wCobine = w;
		return wCobine;
	}
}
