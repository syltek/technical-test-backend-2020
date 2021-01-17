package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.data.Wallet;
import com.playtomic.tests.wallet.data.dto.WalletDTO;
import com.playtomic.tests.wallet.repositories.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.impl.ThirdPartyPaymentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

	@Autowired
	private WalletRepository walletRepository;

	private ThirdPartyPaymentService thirdPartyPaymentService;

	@Before
	public void setUp() {
		thirdPartyPaymentService = new ThirdPartyPaymentService(walletRepository);
	}

	@Test(expected = PaymentServiceException.class)
	public void test_exception() throws PaymentServiceException {
		thirdPartyPaymentService.charge(new BigDecimal(5));
	}

	@Test
	@Transactional
	public void test_ok() throws PaymentServiceException {
		thirdPartyPaymentService.charge(new BigDecimal(15));
	}

	@Test(expected = PaymentServiceException.class)
	public void test_exception_recharge() throws PaymentServiceException {
		thirdPartyPaymentService.reCharge(getWalletDTO_fail());
	}

	@Test
	@Transactional
	public void test_ok_recharge() throws PaymentServiceException {
		thirdPartyPaymentService.reCharge(getWalletDTO());
		Wallet wallet = walletRepository.findOne(1L);
		Assert.assertEquals(wallet.getBalance() , BigDecimal.valueOf(20000.0).setScale(2, RoundingMode.CEILING));
	}

	@Test
	public void getInfo_success() throws PaymentServiceException {
		WalletDTO wallet = thirdPartyPaymentService.getInfo("1");
		Assert.assertEquals(wallet.getBalance(), BigDecimal.valueOf(10000.0).setScale(2, RoundingMode.CEILING));
		Assert.assertEquals(wallet.getFirstName(), "Jasmin");
		Assert.assertEquals(wallet.getSecondName(), "Merusic");
	}

	@Test(expected = PaymentServiceException.class)
	public void getInfo_fail() throws PaymentServiceException {
		thirdPartyPaymentService.getInfo("54");
		fail("User not found!!");
	}



	private WalletDTO getWalletDTO() {
		WalletDTO walletDTO = new WalletDTO();
		walletDTO.setId(1L);
		walletDTO.setBalance(BigDecimal.valueOf(10000L));
		walletDTO.setFirstName("Jasmin");
		walletDTO.setSecondName("Merusic");

		return walletDTO;
	}

	private WalletDTO getWalletDTO_fail() {
		WalletDTO walletDTO = new WalletDTO();
		walletDTO.setId(1L);
		walletDTO.setBalance(BigDecimal.valueOf(5L));
		walletDTO.setFirstName("Jasmin");
		walletDTO.setSecondName("Merusic");

		return walletDTO;
	}
}
