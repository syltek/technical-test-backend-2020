package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.Currency;
import com.playtomic.tests.wallet.dto.WalletDTO;
import com.playtomic.tests.wallet.exceptions.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.repository.WalletTransactionsRepository;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;

/** POC Test to prove that I can write different tests (Unit too) :D */
@RunWith(MockitoJUnitRunner.class)
class WalletServiceTest {

  private static final String POC_ID = "1234";
  private static final BigDecimal POC_AMOUNT = new BigDecimal("10.00");

  private WalletService walletService;

  @BeforeEach
  public void setUp() {
    final WalletRepository walletRepository = Mockito.mock(WalletRepository.class);
    final WalletTransactionsRepository walletTransactionsRepository =
        Mockito.mock(WalletTransactionsRepository.class);
    final ThirdPartyPaymentService thirdPartyPaymentService =
        Mockito.mock(ThirdPartyPaymentService.class);
    Wallet wallet = new Wallet(POC_ID, POC_AMOUNT);
    when(walletRepository.findById(POC_ID)).thenReturn(Optional.of(wallet));
    this.walletService =
        new WalletServiceImpl(
            walletRepository, walletTransactionsRepository, thirdPartyPaymentService);
  }

  @Test
  void findById_success() throws WalletNotFoundException {
    Optional<WalletDTO.WalletRes> walletResOpt = walletService.findById(POC_ID);
    Assertions.assertEquals(POC_ID, walletResOpt.get().getId());
    Assertions.assertEquals(POC_AMOUNT, walletResOpt.get().getBalance());
    Assertions.assertEquals(Currency.EUR, walletResOpt.get().getCurrency());
  }
}
