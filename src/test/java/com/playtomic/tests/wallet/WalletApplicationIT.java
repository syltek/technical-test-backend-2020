package com.playtomic.tests.wallet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.json.WalletMoveMoney;
import com.playtomic.tests.wallet.service.IWalletService;
import com.playtomic.tests.wallet.store.Wallet;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class WalletApplicationIT {
	
	@MockBean
	private IWalletService walletService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	public void mockCreateWalletTest() throws JsonProcessingException, Exception {
		Wallet requestWallet = new Wallet();
		requestWallet.setName("Cartera_test");
		requestWallet.setBalance(new BigDecimal("100.00"));
		
		Wallet wallet = new Wallet();
		wallet.setName(requestWallet.getName());
		wallet.setBalance(requestWallet.getBalance());
		
		when(walletService.save(any(Wallet.class))).thenReturn(wallet);
		
		mockMvc.perform(post("/wallets")
	            .contentType("application/json")
	            .content(objectMapper.writeValueAsBytes(requestWallet)))
				.andExpect(jsonPath("$.codResponse", Matchers.is("WALLET_CREATED")))
				.andExpect(jsonPath("$.wallet.name", Matchers.is("Cartera_test")));
	}
	
	@Test
	public void createWalletTest() throws JsonProcessingException, Exception {
		Wallet w = new Wallet();
		w.setName("Cartera_test_creada");
		w.setBalance(new BigDecimal("100.00"));
		
		mockMvc.perform(post("/wallets")
	            .contentType("application/json")
	            .content(objectMapper.writeValueAsBytes(w)))
				.andExpect(jsonPath("$.codResponse", Matchers.is("WALLET_CREATED")))
				.andExpect(jsonPath("$.wallet.name", Matchers.is("Cartera_test_creada")));
		
	}
	
	@Test
	public void getAllWalletsTest() throws JsonProcessingException, Exception {
		Wallet w = new Wallet();
		w.setName("Cartera_test");
		w.setBalance(new BigDecimal("100.00"));
		
		mockMvc.perform(post("/wallets").contentType("application/json")
	            .content(objectMapper.writeValueAsBytes(w)))
				.andExpect(jsonPath("$.codResponse", Matchers.is("WALLET_CREATED")));
		
		mockMvc.perform(post("/wallets").contentType("application/json")
	            .content(objectMapper.writeValueAsBytes(w)))
				.andExpect(jsonPath("$.codResponse", Matchers.is("WALLET_CREATED")));
		
		this.mockMvc.perform(get("/wallets"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", Matchers.is(2)));
				
	}
	
	@Test
	public void walletChargeTest() throws JsonProcessingException, Exception {
		Wallet w = new Wallet();
		w.setName("Cartera_test");
		w.setBalance(new BigDecimal("100.00"));
		
		mockMvc.perform(post("/wallets").contentType("application/json")
	            .content(objectMapper.writeValueAsBytes(w)))
				.andExpect(jsonPath("$.codResponse", Matchers.is("WALLET_CREATED")));
		
		
		WalletMoveMoney wmm = new WalletMoveMoney();
		wmm.setAmount(new BigDecimal("50"));
		
		mockMvc.perform(post("/wallets/{id}/charge",1).contentType("application/json")
	            .content(objectMapper.writeValueAsBytes(wmm)))
				.andExpect(jsonPath("$.wallet.balance", Matchers.is(150.0)));
		
	}
	
	
	@Test
	public void updateWalletTest() throws JsonProcessingException, Exception {
		Wallet w = new Wallet();
		w.setName("Cartera_test");
		w.setBalance(new BigDecimal("100.00"));
		
		
		mockMvc.perform(post("/wallets").contentType("application/json")
	            .content(objectMapper.writeValueAsBytes(w)))
				.andExpect(jsonPath("$.codResponse", Matchers.is("WALLET_CREATED")));
		
		Wallet walletForUP = new Wallet();
		walletForUP.setIdWallet(1);
		walletForUP.setName("Cartera_test_update");
		walletForUP.setBalance(new BigDecimal("100.00"));
		
		mockMvc.perform(post("/wallets")
	            .contentType("application/json")
	            .content(objectMapper.writeValueAsBytes(walletForUP)))
				.andExpect(jsonPath("$.codResponse", Matchers.is("WALLET_UPDATED")))
				.andExpect(jsonPath("$.wallet.name", Matchers.is("Cartera_test_update")));
		
		
	}
	
}
