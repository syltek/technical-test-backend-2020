package com.playtomic.tests.wallet.controller;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.playtomic.tests.wallet.api.WalletController;
import com.playtomic.tests.wallet.dto.Payment;
import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.exception.PaymentException;
import com.playtomic.tests.wallet.exception.PaymentServiceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.service.WalletService;

@RunWith(MockitoJUnitRunner.class)
public class WalletControllerTest {

	@InjectMocks
	private WalletController controller = new WalletController();
	
    @Mock
    private WalletService walletService;
    
    @Test
    public void getWalletById() throws WalletNotFoundException {
    	String uniqueId = UUID.randomUUID().toString();
        Wallet wallet = new Wallet(uniqueId, new BigDecimal(100));
        Mockito.when(walletService.findById(uniqueId)).thenReturn(wallet);

        ResponseEntity<Wallet> walletResponse = controller.getWalletById(1, uniqueId);
        
        assertEquals(walletResponse.getBody().getId(), wallet.getId());
        assertThat(walletResponse.getBody().getBalance(),  Matchers.comparesEqualTo(new BigDecimal(100.00)));
        
        assertEquals(walletResponse.getStatusCodeValue(), 200);
    }
    
    @Test(expected = WalletNotFoundException.class)
    public void getWalletByIdKO() throws WalletNotFoundException {
    	String uniqueId = UUID.randomUUID().toString();
        Mockito.doThrow(new WalletNotFoundException()).when(walletService).findById(uniqueId);
        controller.getWalletById(1, uniqueId);
    }
    
    @Test
    public void rechargeWallet() throws WalletNotFoundException, PaymentServiceException, PaymentException {
    	String uniqueId = UUID.randomUUID().toString();
        Wallet wallet = new Wallet(uniqueId, new BigDecimal(140));
        Payment payment = new Payment(uniqueId, new BigDecimal(20));
        
        Mockito.when(walletService.rechargeWallet(uniqueId, payment.getAmount())).thenReturn(wallet);

        ResponseEntity<Wallet> walletResponse = controller.rechargeWallet(1, payment);
        
        assertEquals(walletResponse.getBody().getId(), wallet.getId());
        assertThat(walletResponse.getBody().getBalance(),  Matchers.comparesEqualTo(wallet.getBalance()));
        
        assertEquals(walletResponse.getStatusCodeValue(), 200);
    }
    
    @Test(expected = WalletNotFoundException.class)
    public void rechargeWalletKO() throws WalletNotFoundException, PaymentServiceException, PaymentException {
    	String uniqueId = UUID.randomUUID().toString();
        Payment payment = new Payment(uniqueId, new BigDecimal(20));
        Mockito.doThrow(new WalletNotFoundException()).when(walletService).rechargeWallet(uniqueId, payment.getAmount());

        controller.rechargeWallet(1, payment);
    }
    
    @Test(expected = PaymentServiceException.class)
    public void rechargeWalletKOPayment() throws PaymentServiceException, WalletNotFoundException, PaymentException {
    	String uniqueId = UUID.randomUUID().toString();
        Payment payment = new Payment(uniqueId, new BigDecimal(20));
        Mockito.doThrow(new PaymentServiceException()).when(walletService).rechargeWallet(uniqueId, payment.getAmount());

        controller.rechargeWallet(1, payment);
    }
    
    @Test
    public void makePaymentWallet() throws WalletNotFoundException, PaymentException {
    	String uniqueId = UUID.randomUUID().toString();
        Wallet wallet = new Wallet(uniqueId, new BigDecimal(100));
        Payment payment = new Payment(uniqueId, new BigDecimal(20));
        
        Mockito.when(walletService.makePayment(uniqueId, payment.getAmount())).thenReturn(wallet);

        ResponseEntity<Wallet> walletResponse = controller.makePayment(1, payment);
        
        assertEquals(walletResponse.getBody().getId(), wallet.getId());
        assertThat(walletResponse.getBody().getBalance(),  Matchers.comparesEqualTo(wallet.getBalance()));
        
        assertEquals(walletResponse.getStatusCodeValue(), 200);
    }
    
    @Test(expected = WalletNotFoundException.class)
    public void makePaymentWalletKO() throws WalletNotFoundException, PaymentException {
    	String uniqueId = UUID.randomUUID().toString();
        Payment payment = new Payment(uniqueId, new BigDecimal(20));
        Mockito.doThrow(new WalletNotFoundException()).when(walletService).makePayment(uniqueId, payment.getAmount());

        controller.makePayment(1, payment);
    }
    
    @Test(expected = PaymentException.class)
    public void makePaymentWalletKOBalance() throws PaymentException, WalletNotFoundException {
    	String uniqueId = UUID.randomUUID().toString();
        Payment payment = new Payment(uniqueId, new BigDecimal(20));
        Mockito.doThrow(new PaymentException()).when(walletService).makePayment(uniqueId, payment.getAmount());

        controller.makePayment(1, payment);
    }
    
    
    
    
}