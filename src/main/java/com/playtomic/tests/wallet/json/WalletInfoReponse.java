package com.playtomic.tests.wallet.json;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.playtomic.tests.wallet.store.Wallet;

public class WalletInfoReponse {
	
	private Wallet Wallet;
	private String codResponse;
	private String message;
	
	
	
	public WalletInfoReponse(com.playtomic.tests.wallet.store.Wallet wallet, String codResponse, String message) {
		Wallet = wallet;
		this.codResponse = codResponse;
		this.message = message;
	}
	
	public Wallet getWallet() {
		return Wallet;
	}
	public void setWallet(Wallet wallet) {
		Wallet = wallet;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCodResponse() {
		return codResponse;
	}
	public void setCodResponse(String codResponse) {
		this.codResponse = codResponse;
	}
	
	
	
	
	
	
	

}
