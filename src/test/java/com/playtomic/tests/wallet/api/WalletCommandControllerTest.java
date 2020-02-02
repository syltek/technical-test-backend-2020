package com.playtomic.tests.wallet.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.exception.WalletChargeException;
import com.playtomic.tests.wallet.exception.WalletFoundException;
import com.playtomic.tests.wallet.exception.WalletNotEnoughBalanceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.dto.WalletChargeDTO;
import com.playtomic.tests.wallet.service.WalletCommandService;


@WebMvcTest(controllers = WalletCommandController.class)
public class WalletCommandControllerTest {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WalletCommandService walletService;
	
		
	@Test
	void givenNonExistentWallet_whenCreateWallet_thenReturnsCreated() throws Exception {
		Wallet wallet = new Wallet(1, 1000D);
		WalletChargeDTO walletDTO = new WalletChargeDTO(1, 1000D);
		
		when(walletService.createWallet(any())).thenReturn(wallet);
		
		mockMvc.perform(
				post("/api/v1/wallets")
				.content(objectMapper.writeValueAsString(walletDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
			;
		
		verify(walletService, times(1)).createWallet(any());
	}
	
	@Test
	void givenExistentUser_whenCreateUser_thenReturnsConflict() throws Exception {
		
		WalletChargeDTO walletDTO = new WalletChargeDTO(1, 1000D);
	    
		when(walletService.createWallet(any())).thenThrow(WalletFoundException.class);
		
		mockMvc.perform(
				post("/api/v1/wallets")
				.content(objectMapper.writeValueAsString(walletDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isConflict())
			;
		
		verify(walletService, times(1)).createWallet(any());
	}
	
	@Test
	void givenNonExistentWallet_whenPay_thenReturnNotFound() throws Exception {
		WalletChargeDTO walletDTO = new WalletChargeDTO(1, 1000D);
		
		Mockito.doThrow(WalletNotFoundException.class).when(walletService).payment(any(), any());
		
		mockMvc.perform(
				put("/api/v1/wallets/pay")
				.content(objectMapper.writeValueAsString(walletDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound())
			;
		
		verify(walletService, times(1)).payment(any(), any());
	}
	
	@Test
	void givenExistentWallet_whenPayment_thenReturnsNoContent() throws Exception {
		WalletChargeDTO walletDTO = new WalletChargeDTO(1, 1000.0);
		
		mockMvc.perform(
				put("/api/v1/wallets/pay")
				.content(objectMapper.writeValueAsString(walletDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent())
			;
		
		verify(walletService, times(1)).payment(any(), any());
	}
	
	@Test
	void givenExistentWalletNotEnoughBalance_whenPayment_thenReturnsException() throws Exception {
		WalletChargeDTO walletDTO = new WalletChargeDTO(1, 1000.0);
		
		Mockito.doThrow(WalletNotEnoughBalanceException.class).when(walletService).payment(any(), any());
		
		mockMvc.perform(
				put("/api/v1/wallets/pay")
				.content(objectMapper.writeValueAsString(walletDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isPreconditionFailed())
			;
		
		verify(walletService, times(1)).payment(any(), any());
	}
	
	@Test
	void givenNonExistentWallet_whenCharge_thenReturnNotFound() throws Exception {
		WalletChargeDTO walletDTO = new WalletChargeDTO(1, 1000D);
		
		Mockito.doThrow(WalletNotFoundException.class).when(walletService).charge(any(), any());
		
		mockMvc.perform(
				put("/api/v1/wallets/charge")
				.content(objectMapper.writeValueAsString(walletDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound())
			;
		
		verify(walletService, times(1)).charge(any(), any());
	}
	
	@Test
	void givenExistentWallet_whenCharge_thenReturnsNoContent() throws Exception {
		WalletChargeDTO walletDTO = new WalletChargeDTO(1, 1000.0);
		
		mockMvc.perform(
				put("/api/v1/wallets/charge")
				.content(objectMapper.writeValueAsString(walletDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent())
			;
		
		verify(walletService, times(1)).charge(any(), any());
	}
	
	@Test
	void givenExistentWallet_whenChargeLessThan10_thenReturnsException() throws Exception {
		WalletChargeDTO walletDTO = new WalletChargeDTO(1, 1000.0);
		
		Mockito.doThrow(WalletChargeException.class).when(walletService).charge(any(), any());
		
		mockMvc.perform(
				put("/api/v1/wallets/charge")
				.content(objectMapper.writeValueAsString(walletDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isPreconditionFailed())
			;
		
		verify(walletService, times(1)).charge(any(), any());
	}
}
