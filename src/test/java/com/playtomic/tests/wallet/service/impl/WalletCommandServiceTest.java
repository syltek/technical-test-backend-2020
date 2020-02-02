package com.playtomic.tests.wallet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.playtomic.tests.wallet.exception.WalletChargeException;
import com.playtomic.tests.wallet.exception.WalletFoundException;
import com.playtomic.tests.wallet.exception.WalletNotEnoughBalanceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;


@ExtendWith(MockitoExtension.class)
public class WalletCommandServiceTest {

	@Mock
	private WalletRepository walletRepository;
	
	@Mock
	private PaymentService paymentService;
	
	@InjectMocks
	private WalletCommandServiceImpl walletService;
	
	
	
	@Test
	void givenWalletWithoutId_whenCreateWallet_thenReturnsWallet() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(null, 1000.0);
		Wallet walletCreated = new Wallet(1, 1000.0);
		
		when(walletRepository.save(any())).thenReturn(walletCreated);
	    
//		WHEN
		Wallet savedUser = walletService.createWallet(wallet);
		
//		THEN
		assertNotNull(savedUser);
		assertEquals(savedUser.getId(), 1);
		
		verify(walletRepository, times(0)).findById(any());
		verify(walletRepository, times(1)).save(any());
	}
	
	@Test
	void givenWalletWitId_whenCreateWallet_thenReturnsWallet() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(1, 1000.0);
		
		when(walletRepository.findById(1)).thenReturn(Optional.empty());
		when(walletRepository.save(any())).thenReturn(wallet);
	    
//		WHEN
		Wallet savedUser = walletService.createWallet(wallet);
		
//		THEN
		assertNotNull(savedUser);
		assertEquals(savedUser.getId(), 1);
		
		verify(walletRepository, times(1)).findById(any());
		verify(walletRepository, times(1)).save(any());
	}
	
	@Test
	void givenWalletAlreadyPresent_whenCreateWallet_thenReturnsWalletFoundException() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(1, 1000.0);
		
		when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
	    
//		WHEN
		assertThrows(WalletFoundException.class, () -> walletService.createWallet(wallet));
		
//		THEN
		verify(walletRepository, times(1)).findById(1);
		verify(walletRepository, times(0)).save(any());
	}
	
	@Test
	void givenExistentWallet_whenCharge_thenAllOk() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(1, 1000.0);
		Wallet savedWallet = new Wallet(1, 1100.0);
		
		when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
		when(walletRepository.save(wallet)).thenReturn(savedWallet);
		Mockito.doNothing().when(paymentService).charge(any());
		
//		WHEN - THEN
		walletService.charge(1, 100.0);
		
//		THEN
		verify(walletRepository, times(1)).findById(1);
		verify(walletRepository, times(1)).save(any());
		verify(paymentService, times(1)).charge(any());
	}
	
	@Test
	void givenExistentWallet_whenChargeLessThan10_thenWalletChargeException() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(1, 1000.0);
		
		when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
		Mockito.doThrow(PaymentServiceException.class).when(paymentService).charge(any());
		
//		WHEN - THEN
		assertThrows(WalletChargeException.class, () -> walletService.charge(1, 5.0));
		
//		THEN
		verify(walletRepository, times(1)).findById(1);
		verify(walletRepository, times(0)).save(wallet);
		verify(paymentService, times(1)).charge(any());
	}
	
	@Test
	void givenNonExistentWallet_whenCharge_thenWalletNotFoundException() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(1, 1000.0);
		
		when(walletRepository.findById(1)).thenReturn(Optional.empty());
		
//		WHEN - THEN
		assertThrows(WalletNotFoundException.class, () -> walletService.charge(1, 2000.0));
		
//		THEN
		verify(walletRepository, times(1)).findById(1);
		verify(walletRepository, times(0)).save(wallet);
	}
	
	@Test
	void givenExistentWallet_whenPayment_thenAllOk() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(1, 1000.0);
		
		when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
		
//		WHEN - THEN
		walletService.payment(1, 100.0);
		
//		THEN
		verify(walletRepository, times(1)).findById(1);
		verify(walletRepository, times(1)).save(wallet);
	}
	
	@Test
	void givenExistentWallet_whenPaymentNotEnoughBalance_thenWalletNotEnoughException() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(1, 1000.0);
		
		when(walletRepository.findById(1)).thenReturn(Optional.of(wallet));
		
//		WHEN - THEN
		assertThrows(WalletNotEnoughBalanceException.class, () -> walletService.payment(1, 2000.0));
		
//		THEN
		verify(walletRepository, times(1)).findById(1);
		verify(walletRepository, times(0)).save(wallet);
	}
	
	@Test
	void givenNonExistentWallet_whenPayment_thenWalletNotFoundException() throws Exception {
//		GIVEN
		Wallet wallet = new Wallet(1, 1000.0);
		
		when(walletRepository.findById(1)).thenReturn(Optional.empty());
		
//		WHEN - THEN
		assertThrows(WalletNotFoundException.class, () -> walletService.payment(1, 2000.0));
		
//		THEN
		verify(walletRepository, times(1)).findById(1);
		verify(walletRepository, times(0)).save(wallet);
	}
}
