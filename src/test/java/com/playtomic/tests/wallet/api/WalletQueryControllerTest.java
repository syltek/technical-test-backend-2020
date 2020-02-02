package com.playtomic.tests.wallet.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToCompressingWhiteSpace;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.dto.WalletQueryDTO;
import com.playtomic.tests.wallet.service.WalletQueryService;


@WebMvcTest(controllers = WalletQueryController.class)
public class WalletQueryControllerTest {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WalletQueryService walletService;
	
	@Test
	void whenGetAllWallets_thenReturnsOK() throws Exception {
		
		List<Wallet> allWalletsList = Stream.of(
				new Wallet(1, 1000D),
				new Wallet(2, 5.5)
			).collect(Collectors.toList());
		
		List<WalletQueryDTO> expectedWalletsList = Stream.of(
				new WalletQueryDTO(1, 1000D),
				new WalletQueryDTO(2, 5.5)
			).collect(Collectors.toList());
		
		when(walletService.searchAll()).thenReturn(allWalletsList);
	        
		MvcResult mvcResult = mockMvc.perform(
				get("/api/v1/wallets")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
				.andReturn()
			;
		
		
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(objectMapper.writeValueAsString(expectedWalletsList),
				equalToCompressingWhiteSpace(actualResponseBody));
		
		verify(walletService, times(1)).searchAll();
	    verifyNoMoreInteractions(walletService);
	}	

	@Test
	void whenGetWallet_thenReturnsOK() throws Exception {
		Wallet wallet = new Wallet(1, 1000D);
		WalletQueryDTO walletDTO = new WalletQueryDTO(1, 1000D);
		
		
	    when(walletService.searchById(1)).thenReturn(wallet);
		
	    MvcResult mvcResult = mockMvc.perform(
				get("/api/v1/wallets/{id}", 1)
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id", is(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$.balance", is(1000.0)))
				.andReturn()
			;
		
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertThat(objectMapper.writeValueAsString(walletDTO),
				equalToCompressingWhiteSpace(actualResponseBody));
		
		verify(walletService, times(1)).searchById(1);
	}

	@Test
	void givenNonExistentUser_whenGetUser_thenReturnsNotFound() throws Exception {
		
		Mockito.doThrow(WalletNotFoundException.class).when(walletService).searchById(any());

        mockMvc.perform(get("/api/v1/wallets/{id}", 1))
                .andExpect(status().isNotFound());

        verify(walletService, times(1)).searchById(any());
	}

}
