package com.playtomic.tests.wallet.model.dto;

public class WalletQueryDTO {

	private int id;
	private double balance;
	
	public WalletQueryDTO() {
	}
	
	public WalletQueryDTO(int id, double balance) {
		this.id = id;
		this.balance = balance;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
}
