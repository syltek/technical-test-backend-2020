package com.playtomic.tests.wallet.repository;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.playtomic.tests.wallet.store.Wallet;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles(profiles = "test")
public class WalletRepositoryTest {
	
	@Autowired    
	TestEntityManager entityManager;
	
	@Autowired
	IWalletRepository iWalletRepo;

	@Test   
	public void it_should_save_user() {
		Wallet wallet = new Wallet();
		wallet.setName("Cartera_prueba");
		wallet = entityManager.persistAndFlush(wallet);
		
		assertThat(iWalletRepo.findOne(wallet.getIdWallet()), Matchers.is(wallet));
	}
}
