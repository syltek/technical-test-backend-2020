package com.playtomic.tests.wallet.store;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity(name = "WalletMovement")
@Table(name = "walletmovement")
public class WalletMovement{

	@Id
	@GeneratedValue
	private int idWalletMovement;

	private String movement;

	private BigDecimal amount;

	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss" , timezone="Europe/Zagreb")
	private Date dateMovement;

	public WalletMovement() {}
	
	public WalletMovement(String movement, BigDecimal amount,
			Date dateMovement) {
		
		this.movement = movement;
		this.amount = amount;
		this.dateMovement = dateMovement;
	}

	public int getIdWalletMovement() {
		return idWalletMovement;
	}

	public void setIdWalletMovement(int idWalletMovement) {
		this.idWalletMovement = idWalletMovement;
	}

	public String getMovement() {
		return movement;
	}

	public void setMovement(String movement) {
		this.movement = movement;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getDateMovement() {
		return dateMovement;
	}

	public void setDateMovement(Date dateMovement) {
		this.dateMovement = dateMovement;
	}

	

}
