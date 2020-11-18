package com.playtomic.tests.wallet.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.playtomic.tests.wallet.dto.Wallet;

@Component
public class WalletMapper {

	public Wallet convertToDTO(WalletDAO wallet) {
		return new Wallet(wallet.getId(), wallet.getBalance());
	}
	
	public WalletDAO convertFromDTO(Wallet wallet) {
		return new WalletDAO(wallet.getId(), wallet.getBalance());
	}
	
	public List<Wallet> convertAllToDTO(List<WalletDAO> wallets) {
		return wallets.stream().map(b -> convertToDTO(b)).collect(Collectors.toList());
	}
	
	public List<WalletDAO> convertAllFromDTO(List<Wallet> wallets) {
		return wallets.stream().map(b -> convertFromDTO(b)).collect(Collectors.toList());
	}

}