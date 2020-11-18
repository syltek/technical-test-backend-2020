package com.playtomic.tests.wallet.repository;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.playtomic.tests.wallet.model.WalletDAO;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class WalletRepositoryTest {

	@Autowired
	private WalletRepository repo;
	
	@Test
	public void testSaveWallet() {
		WalletDAO wallet = new WalletDAO(UUID.randomUUID().toString(), new BigDecimal(100.00));
		
		WalletDAO walletSaved = repo.save(wallet);
		
		assertEquals(wallet.getId(), walletSaved.getId());
		assertThat(wallet.getBalance(),  Matchers.comparesEqualTo(walletSaved.getBalance()));
	}
	
	@Test
	public void testGetWallet() {
		WalletDAO wallet = new WalletDAO(UUID.randomUUID().toString(), new BigDecimal(100.00));
		repo.save(wallet);
		
		Optional<WalletDAO> recoveredWallet = repo.findById(wallet.getId());
		
		assertEquals(wallet.getId(), recoveredWallet.get().getId());
		assertThat(wallet.getBalance(),  Matchers.comparesEqualTo(recoveredWallet.get().getBalance()));
	}
	
	@Test
	public void testKOGetWallet() {
		WalletDAO wallet = new WalletDAO(UUID.randomUUID().toString(), new BigDecimal(100.00));
		
		Optional<WalletDAO> recoveredWallet = repo.findById(wallet.getId());
		
		assertEquals(false, recoveredWallet.isPresent());
	}
	
	@Test
	public void testUpdateBalance() {
		WalletDAO wallet = new WalletDAO(UUID.randomUUID().toString(), new BigDecimal(100.00));
		repo.save(wallet);
		
		Optional<WalletDAO> recoveredWallet = repo.findById(wallet.getId());
		
		assertEquals(wallet.getId(), recoveredWallet.get().getId());
		assertThat(recoveredWallet.get().getBalance(),  Matchers.comparesEqualTo(new BigDecimal(100.00)));
		
		wallet.setBalance(wallet.getBalance().add(new BigDecimal(500.00)));

		repo.save(wallet);
		
		recoveredWallet = repo.findById(wallet.getId());
		
		assertEquals(wallet.getId(), recoveredWallet.get().getId());
		assertThat(recoveredWallet.get().getBalance(),  Matchers.comparesEqualTo(new BigDecimal(600.00)));
	}
	
    
}