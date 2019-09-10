package com.playtomic.tests.wallet.store;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

@Entity(name = "Wallet")
@Table(name = "wallet")
public class Wallet {
	
	@Id
	@GeneratedValue
	private int idWallet;
	private String name;
	
	@Digits(integer=7, fraction=2)
	private BigDecimal balance;
	
	@OneToMany(cascade = CascadeType.ALL) 
    private List<WalletMovement> walletMovement = new ArrayList<>();
	
	public int getIdWallet() {
		return idWallet;
	}
	public void setIdWallet(int idWallet) {
		this.idWallet = idWallet;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public List<WalletMovement> getWalletMovement() {
		return walletMovement;
	}
	public void setWalletMovement(List<WalletMovement> walletMovement) {
		this.walletMovement = walletMovement;
	}
	
	public void addMovement(String movement, BigDecimal amount) {
        WalletMovement walletMovement = new WalletMovement(movement,amount,new Date());
        this.walletMovement.add(walletMovement);
    }
	
	

}
