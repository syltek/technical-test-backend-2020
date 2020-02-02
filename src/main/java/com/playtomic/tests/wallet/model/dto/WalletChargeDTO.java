package com.playtomic.tests.wallet.model.dto;

public class WalletChargeDTO {

	private int id;
	private double amount;
	
	public WalletChargeDTO() {
	}
	
	public WalletChargeDTO(int id, double amount) {
		this.id = id;
		this.amount = amount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
