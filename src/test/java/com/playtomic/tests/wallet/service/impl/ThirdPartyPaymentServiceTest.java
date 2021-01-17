package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.data.Wallet;
import com.playtomic.tests.wallet.data.dto.WalletDTO;
import com.playtomic.tests.wallet.repositories.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.Matchers.any;

public class ThirdPartyPaymentServiceTest {


    @InjectMocks
    private ThirdPartyPaymentService thirdPartyPaymentService;

    @Mock
    private WalletRepository walletRepository;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = PaymentServiceException.class)
    public void test_exception() throws PaymentServiceException {
        thirdPartyPaymentService.charge(new BigDecimal(5));
    }

    @Test
    public void test_ok() throws PaymentServiceException {
        Wallet wallet = new Wallet(1L,"Jasmin","Merusic",BigDecimal.valueOf(10000L));
        Mockito.when(walletRepository.findOne(1L)).thenReturn(wallet);
        thirdPartyPaymentService.charge(new BigDecimal(15));
        Mockito.verify(walletRepository,Mockito.times(1)).save(any(Wallet.class));
    }

}