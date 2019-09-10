package com.playtomic.tests.wallet.service.impl;

import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.playtomic.tests.wallet.service.IWalletService;
import com.playtomic.tests.wallet.store.Wallet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletServiceTest {
	
	@MockBean
	private IWalletService walletService;
	
	@Test
	public void getAllWalletsTest() throws JsonProcessingException, Exception {
		Wallet requestWallet = new Wallet();
		requestWallet.setName("Cartera_test");
		requestWallet.setBalance(new BigDecimal("100.00"));
		
		when(walletService.save(any(Wallet.class))).thenReturn(new Wallet());
		
		Wallet walletCreated = walletService.save(requestWallet);
		
		assertThat(walletCreated.getBalance(), Matchers.is(requestWallet.getBalance()));
	}

}
