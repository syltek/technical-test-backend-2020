package com.playtomic.tests.wallet.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.exception.PaymentException;
import com.playtomic.tests.wallet.exception.PaymentServiceException;
import com.playtomic.tests.wallet.exception.WalletAlreadyExistsException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.WalletDAO;
import com.playtomic.tests.wallet.model.WalletMapper;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletService;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceTest {

	@InjectMocks
    private WalletService service = new WalletServiceImpl();
	
	@Mock
	private ThirdPartyPaymentService thirdPartyPaymentService;
	
	@Mock
	private WalletRepository walletRepo;
	
	@Mock
	private WalletMapper mapper;
	
	@Test
    public void getAllWallets() {
		String uniqueId1 = UUID.randomUUID().toString();
		String uniqueId2 = UUID.randomUUID().toString();

		List<WalletDAO> walletsDAO = new ArrayList<>();
		walletsDAO.add(new WalletDAO(uniqueId1, new BigDecimal(500.00)));
		walletsDAO.add(new WalletDAO(uniqueId2, new BigDecimal(100.00)));
		
		List<Wallet> wallets = new ArrayList<>();
		wallets.add(new Wallet(uniqueId1, new BigDecimal(500.00)));
		wallets.add(new Wallet(uniqueId2, new BigDecimal(100.00)));
		
		Mockito.when(walletRepo.findAll()).thenReturn(walletsDAO);
		Mockito.when(mapper.convertAllToDTO(walletsDAO)).thenReturn(wallets);
		
		List<Wallet> recoveredWallets = service.findAll();
		
		assertEquals(recoveredWallets.size(), 2);
    }
	
	@Test
    public void saveWallet() throws WalletAlreadyExistsException {
		String uniqueId = UUID.randomUUID().toString();
		
		Wallet wallet = new Wallet(uniqueId, new BigDecimal(500.00));
		WalletDAO walletDAO = new WalletDAO(uniqueId, new BigDecimal(500.00));
		
		Mockito.when(mapper.convertFromDTO(wallet)).thenReturn(walletDAO);
		Mockito.when(mapper.convertToDTO(walletDAO)).thenReturn(wallet);
		Mockito.when(walletRepo.save(walletDAO)).thenReturn(walletDAO);
		
		Wallet resultWallet = service.saveWallet(wallet);
		
		assertEquals(wallet.getId(), resultWallet.getId());
		assertThat(resultWallet.getBalance(),  Matchers.comparesEqualTo(new BigDecimal(500.00)));
    }
	
	@Test
    public void getWalletById() throws WalletNotFoundException {
		String uniqueId = UUID.randomUUID().toString();
		WalletDAO walletDAO = new WalletDAO(uniqueId, new BigDecimal(500.00));
		Wallet wallet = new Wallet(uniqueId, new BigDecimal(500.00));
		Mockito.when(walletRepo.findById(wallet.getId())).thenReturn(Optional.of(walletDAO));
		Mockito.when(mapper.convertToDTO(walletDAO)).thenReturn(wallet);

		Wallet recoveredWallet = service.findById(wallet.getId());
		
		assertEquals(wallet.getId(), recoveredWallet.getId());
		assertThat(wallet.getBalance(),  Matchers.comparesEqualTo(recoveredWallet.getBalance()));
    }
	
	@Test(expected = WalletNotFoundException.class)
    public void getWalletByIdKO() throws WalletNotFoundException {
		service.findById("test");
    }
	
	@Test()
    public void rechargeWalletTest() throws WalletNotFoundException, PaymentServiceException {
		//find by id of 500€ wallet
		String uniqueId = UUID.randomUUID().toString();
		WalletDAO walletDAO = new WalletDAO(uniqueId, new BigDecimal(500.00));
		Wallet wallet = new Wallet(uniqueId, new BigDecimal(500.00));
		Mockito.when(walletRepo.findById(wallet.getId())).thenReturn(Optional.of(walletDAO));
		Mockito.when(mapper.convertToDTO(walletDAO)).thenReturn(wallet);
		
		Mockito.doNothing().when(thirdPartyPaymentService).charge(new BigDecimal(50.00));
		
		WalletDAO walletUpdatedDAO = new WalletDAO(uniqueId, new BigDecimal(550.00));
		Wallet walletUpdated = new Wallet(uniqueId, new BigDecimal(550.00));
		Mockito.when(walletRepo.save(walletUpdatedDAO)).thenReturn(walletUpdatedDAO);
		Mockito.when(mapper.convertFromDTO(walletUpdated)).thenReturn(walletUpdatedDAO);
		Mockito.when(mapper.convertToDTO(walletUpdatedDAO)).thenReturn(walletUpdated);
		
		Wallet resultWallet = service.rechargeWallet(uniqueId, new BigDecimal(50.00));
		
		assertEquals(wallet.getId(), resultWallet.getId());
		assertThat(resultWallet.getBalance(),  Matchers.comparesEqualTo(new BigDecimal(550.00)));
		
    }
	
	@Test(expected = PaymentServiceException.class)
    public void rechargeWalletTestKO() throws WalletNotFoundException, PaymentServiceException {
		//find by id of 500€ wallet
		String uniqueId = UUID.randomUUID().toString();
		WalletDAO walletDAO = new WalletDAO(uniqueId, new BigDecimal(500.00));
		Wallet wallet = new Wallet(uniqueId, new BigDecimal(500.00));
		Mockito.when(walletRepo.findById(wallet.getId())).thenReturn(Optional.of(walletDAO));
		Mockito.when(mapper.convertToDTO(walletDAO)).thenReturn(wallet);
		
		
		BigDecimal charge = new BigDecimal(5.00);
		Mockito.doThrow(new PaymentServiceException()).when(thirdPartyPaymentService).charge(charge);
		
		service.rechargeWallet(uniqueId, charge);
    }
	
	@Test()
    public void paymentWalletTest() throws WalletNotFoundException, PaymentException, PaymentServiceException {
		
		//find by id of 500€ wallet
		String uniqueId = UUID.randomUUID().toString();
		WalletDAO walletDAO = new WalletDAO(uniqueId, new BigDecimal(500.00));
		Wallet wallet = new Wallet(uniqueId, new BigDecimal(500.00));
		Mockito.when(walletRepo.findById(wallet.getId())).thenReturn(Optional.of(walletDAO));
		Mockito.when(mapper.convertToDTO(walletDAO)).thenReturn(wallet);
				
		//save wallet of 450€
		WalletDAO walletUpdatedDAO = new WalletDAO(uniqueId, new BigDecimal(450.00));
		Wallet walletUpdated = new Wallet(uniqueId, new BigDecimal(450.00));
		Mockito.when(mapper.convertFromDTO(walletUpdated)).thenReturn(walletUpdatedDAO);
		Mockito.when(walletRepo.save(walletUpdatedDAO)).thenReturn(walletUpdatedDAO);
		Mockito.when(mapper.convertToDTO(walletUpdatedDAO)).thenReturn(walletUpdated);
		
		
		Wallet resultWallet = service.makePayment(uniqueId, new BigDecimal(50.00));
		
		assertEquals(wallet.getId(), resultWallet.getId());
		assertThat(resultWallet.getBalance(),  Matchers.comparesEqualTo(new BigDecimal(450.00)));
		
    }
	
	@Test(expected = PaymentException.class)
    public void paymentWalletTestKO() throws WalletNotFoundException, PaymentException {
		//find by id of 20€ wallet
		String uniqueId = UUID.randomUUID().toString();
		WalletDAO walletDAO = new WalletDAO(uniqueId, new BigDecimal(20.00));
		Wallet wallet = new Wallet(uniqueId, new BigDecimal(20.00));
		Mockito.when(walletRepo.findById(wallet.getId())).thenReturn(Optional.of(walletDAO));
		Mockito.when(mapper.convertToDTO(walletDAO)).thenReturn(wallet);

		//executes test against make Payment
		service.makePayment(uniqueId, new BigDecimal(50.00));
    }
	

}
