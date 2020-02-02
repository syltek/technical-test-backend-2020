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
//	
//	@Test
//	void givenUser_whenCreateUser_thenReturnsUser() throws Exception {
////		GIVEN
//		User expectedUser = User.from(1, "David", LocalDate.now());
//		User user = User.from(null, "David", LocalDate.now());
//		
//		when(walletRepository.save(any())).thenReturn(expectedUser);
//	    
////		WHEN
//		User savedUser = walletService.createUser(user);
//		
////		THEN
//		assertNotNull(savedUser);
//		assertEquals(expectedUser, savedUser);
//		
//		verify(walletRepository, times(0)).findById(any());
//		verify(walletRepository, times(1)).save(any());
//	}
//	
//	@Test
//	void givenExistentUser_whenCreateUser_thenReturnsUserFound() throws Exception {
////		GIVEN
//		User expectedUser = User.from(1, "David", LocalDate.now());
//		
//		when(walletRepository.findById(any())).thenReturn(Optional.of(expectedUser));
//		
////		WHEN - THEN
//		assertThrows(UserFoundException.class, 
//				() -> { walletService.createUser(expectedUser); });
//
////		THEN
//		verify(walletRepository, times(1)).findById(any());
//		verify(walletRepository, times(0)).save(any());
//	}
//	
//	@Test
//	void givenUser_whenUpdateUser_thenReturnsNothing() throws Exception {
////		GIVEN
//		User expectedUser = User.from(1, "David", LocalDate.now());
//		
//		when(walletRepository.findById(any())).thenReturn(Optional.of(expectedUser));
//		when(walletRepository.save(any())).thenReturn(expectedUser);
//	        
////		WHEN
//		walletService.updateUser(expectedUser, 1);
//		
////		THEN
//		verify(walletRepository, times(1)).findById(any());
//		verify(walletRepository, times(1)).save(any());
//	}
//	
//	@Test
//	void givenExistentUser_whenUpdateUser_thenReturnsUserNotFound() throws Exception {
////		GIVEN
//		User expectedUser = User.from(1, "David", LocalDate.now());
//		
//		when(walletRepository.findById(any())).thenReturn(Optional.of(expectedUser));
//		when(walletRepository.save(any())).thenThrow(UserNotFoundException.class);
//		
////		WHEN - THEN
//		assertThrows(UserNotFoundException.class, 
//				() -> { walletService.updateUser(expectedUser, 1); });
//		
////		THEN
//		verify(walletRepository, times(1)).findById(any());
//		verify(walletRepository, times(1)).save(any());
//	}
//	
//	@Test
//	void whenDeleteValidUser_thenReturnsNothing() throws Exception {
////		GIVEN
//		User expectedUser = User.from(1, "David", LocalDate.now());
//		
//		when(walletRepository.findById(any())).thenReturn(Optional.of(expectedUser));
//		Mockito.doNothing().when(walletRepository).deleteById(any());
//	        
////		WHEN
//		walletService.deleteUser(1);
//		
////		THEN
//		verify(walletRepository, times(1)).findById(any());
//		verify(walletRepository, times(1)).deleteById(any());
//	}
//	
//	@Test
//	void whenDeleteNonExistentUser_thenReturnsUserNotFound() throws Exception {
////		GIVEN
//		Mockito.doThrow(UserNotFoundException.class).when(walletRepository).findById(any());
//		
////		WHEN - THEN
//		assertThrows(UserNotFoundException.class, 
//				() -> { walletService.deleteUser( 1); });
//		
////		THEN
//		verify(walletRepository, times(1)).findById(any());
//		verify(walletRepository, times(0)).delete(any());
//	}
}
