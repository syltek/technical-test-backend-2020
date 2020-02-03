package com.playtomic.tests.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;


@ExtendWith(MockitoExtension.class)
public class WalletQueryServiceTest {

	@Mock
	private WalletRepository walletRepository;
	
	@InjectMocks
	private WalletQueryServiceImpl walletService;
	
	@Test
	void whenGetAllWallets_thenReturnsAllWallets() throws Exception {
//		GIVEN
		List<Wallet> allWalletsList = Stream.of(
				new Wallet(1, 1000D),
				new Wallet(2, 5.5)
			).collect(Collectors.toList());
		
		when(walletRepository.findAll()).thenReturn(allWalletsList);
	    
//		WHEN
		List<Wallet> allWallets = walletService.searchAll();
		
//		THEN
		assertNotNull(allWallets);
		
		verify(walletRepository, times(1)).findAll();
	}
	
	
	
	@Test
	void givenValidWallet_whenGetWallet_thenReturnsWallet() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(1, 1000.0);
		
		when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
	        
		Wallet savedWallet = walletService.searchById(1);
		
		assertNotNull(savedWallet);
		assertEquals(savedWallet.getBalance(), 1000.0);
		
		verify(walletRepository, times(1)).findById(1);
	}
	
	@Test
	void givenNoValidWallet_whenGetWallet_thenReturnsWalletNotFound() throws Exception {
//		GIVEN
		when(walletRepository.findById(1)).thenThrow(WalletNotFoundException.class);
		
//		WHEN - THEN
		assertThrows(WalletNotFoundException.class, 
				() -> { walletService.searchById(1); });
		
//		THEN
		verify(walletRepository, times(1)).findById(1);
	}
}
