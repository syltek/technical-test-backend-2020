package com.playtomic.tests.wallet.mapper;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.model.WalletDAO;
import com.playtomic.tests.wallet.model.WalletMapper;

public class WalletMapperTest {
	
	WalletMapper mapper = new WalletMapper();

	@Test
	public void testMapperDTOtoDAO() {
		Wallet wallet = new Wallet(UUID.randomUUID().toString(), new BigDecimal(100.00));
		
		WalletDAO mappedWallet = mapper.convertFromDTO(wallet);
		
		assertEquals(wallet.getId(), mappedWallet.getId());
		assertThat(wallet.getBalance(),  Matchers.comparesEqualTo(mappedWallet.getBalance()));
	}
	
	@Test
	public void testMapperDAOtoDTO() {
		WalletDAO wallet = new WalletDAO(UUID.randomUUID().toString(), new BigDecimal(100.00));
		
		Wallet mappedWallet = mapper.convertToDTO(wallet);
		
		assertEquals(wallet.getId(), mappedWallet.getId());
		assertThat(wallet.getBalance(),  Matchers.comparesEqualTo(mappedWallet.getBalance()));
	}
	
	@Test
	public void testListMapperDTOtoDAO() {
		List<Wallet> wallets = new ArrayList<>();
		wallets.add(new Wallet(UUID.randomUUID().toString(), new BigDecimal(500.00)));
		wallets.add(new Wallet(UUID.randomUUID().toString(), new BigDecimal(100.00)));
		
		List<WalletDAO> mappedWallet = mapper.convertAllFromDTO(wallets);
		
		assertEquals(mappedWallet.size(), 2);
	}
	
	@Test
	public void testListMapperDAOtoDTO() {
		List<WalletDAO> wallets = new ArrayList<>();
		wallets.add(new WalletDAO(UUID.randomUUID().toString(), new BigDecimal(500.00)));
		wallets.add(new WalletDAO(UUID.randomUUID().toString(), new BigDecimal(100.00)));
		
		List<Wallet> mappedWallet = mapper.convertAllToDTO(wallets);
		
		assertEquals(mappedWallet.size(), 2);
	}
	
}