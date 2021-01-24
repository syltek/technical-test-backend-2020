package com.playtomic.tests.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.exception.WalletNotEnoughBalanceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.dto.WalletDto;
import com.playtomic.tests.wallet.model.dto.WalletReChargeReqDto;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WalletApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

	public static String WALLET_FIND_URL = "/api/v1/wallet/%d";
	public static String WALLET_CHARGE_URL = "/api/v1/wallet/wallet-charge";
	public static String WALLET_RE_CHARGE_URL = "/api/v1/wallet/wallet-recharge";


	@Autowired
	private WalletService walletService;

	@Autowired
	WalletController walletController;

	@Autowired
	private MockMvc mockMvc;

	private static final ObjectMapper mapper = new ObjectMapper();

	private Long walletId = 1L;

	ResultMatcher statusOK = MockMvcResultMatchers.status().isOk();
	ResultMatcher notFound = MockMvcResultMatchers.status().isNotFound();

	@Test
	public void findWallet_HttpStatusSuccess() throws Exception {

		final MockHttpServletRequestBuilder getRequest = get(String.format(WALLET_FIND_URL, walletId))
				.contentType(APPLICATION_JSON);

		final MvcResult mvcResult = mockMvc
				.perform(getRequest)
				.andExpect(statusOK)
				.andReturn();

		mockMvc
				.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.currency").value("EUR"))
				.andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(14.00)))
				.andExpect(jsonPath("$.id").value(1));

	}

	@Test(expected = WalletNotFoundException.class)
	public void whenIdIsWrong_thenThrowException()  {
		walletService.findWalletById(32L);
	}

	@Test
	public void chargeAmount_AndSubstract_HttpStatusSuccess() throws Exception {

		final MockHttpServletRequestBuilder postRequest = post(WALLET_CHARGE_URL)
				.contentType(APPLICATION_JSON)
				.content(asString(initWalletDto(BigDecimal.valueOf(11), "EUR")));

		final MvcResult mvcResult = mockMvc
				.perform(postRequest)
				.andExpect(statusOK)
				.andReturn();

		mockMvc
				.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.currency").value("EUR"))
				.andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(14.00)))
				.andExpect(jsonPath("$.id").value(1));

	}

	@Test(expected = WalletNotEnoughBalanceException.class)
	public void chargeAmount_BiggerThanWalletBalance_HttpStatusForbidden(){
		walletService.chargeWallet(initWalletDto(BigDecimal.valueOf(35), "EUR"));
	}

	@Test
	public void reChargeAmountWith_thirdPartyAllowedChannel() throws Exception {

		final MockHttpServletRequestBuilder postRequest = post(WALLET_RE_CHARGE_URL)
				.contentType(APPLICATION_JSON)
				.content(asString(initWalletReChargeReqDto(BigDecimal.valueOf(2000), "PAYPAL")));

		final MvcResult mvcResult = mockMvc
				.perform(postRequest)
				.andExpect(statusOK)
				.andReturn();

		mockMvc
				.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.currency").value("EUR"))
				.andExpect(jsonPath("$.balance").value(BigDecimal.valueOf(2014.00)))
				.andExpect(jsonPath("$.id").value(1));

	}


	private WalletDto initWalletDto(BigDecimal amount, String currency) {
		return WalletDto.builder().id(1L)
				.balance(amount).currency(currency)
				.build();
	}

	private WalletReChargeReqDto initWalletReChargeReqDto(BigDecimal amount, String channel) {
		return WalletReChargeReqDto.builder()
				.chargeAmount(amount)
				.id(1L)
				.paymentChannel(channel)
				.build();
	}

	private String asString(final Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void emptyTest() {
	}
}
