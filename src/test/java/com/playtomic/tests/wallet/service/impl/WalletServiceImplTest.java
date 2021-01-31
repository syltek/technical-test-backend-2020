package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.domain.dto.ChargeDto;
import com.playtomic.tests.wallet.domain.dto.WalletDto;
import com.playtomic.tests.wallet.domain.dto.WithdrawDto;
import com.playtomic.tests.wallet.ex.ErrorException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.repository.WalletTransactionRepository;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static com.playtomic.tests.wallet.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@SpringBootTest
@ActiveProfiles(profiles = "test")
class WalletServiceImplTest {

    @Autowired
    WalletService walletService;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    WalletTransactionRepository walletTransactionRepository;

    @BeforeEach
    void init() {
        initData(walletRepository, walletTransactionRepository);
    }

    @AfterEach
    void clear() {
        destroyData(walletRepository, walletTransactionRepository);
    }

    @Test
    void getWalletById() {
        WalletDto dto = getWalletDto();
        WalletDto result = walletService.getWalletById(TEST_ID);
        assertEquals(result.getWalletId(), dto.getWalletId());
        assertEquals(result.getDescription(), dto.getDescription());
        assertEquals(result.getBalance(), dto.getBalance());
    }

    @Test
    void charge() {
        ChargeDto dto = getChargeDto();
        walletService.charge(dto);
        BigDecimal result = walletTransactionRepository.getBalance(TEST_ID);
        assertEquals(result, BigDecimal.valueOf(222.44));
    }

    @Test
    void withdraw() {
        WithdrawDto dto = getWithdrawDto();
        walletService.withdraw(dto);
        BigDecimal result = walletTransactionRepository.getBalance(TEST_ID);
        assertEquals(result.compareTo(BigDecimal.ZERO), 0);
    }

    @Test
    void thirdPartyPaymentError() {
        ChargeDto dto = getSmallAmountChargeDto();
        Executable executable = () -> walletService.charge(dto);
        assertThrows(ErrorException.class, executable);
    }

    @Test
    void withdrawError() {
        WithdrawDto dto = getBigAmountWithdrawDto();
        Executable executable = () -> walletService.withdraw(dto);
        assertThrows(ErrorException.class, executable);
    }

    @Test
    void withdrawValidateIdError() {
        WithdrawDto dto = getWrongIdWithdrawDto();
        Executable executable = () -> walletService.withdraw(dto);
        assertThrows(ErrorException.class, executable);
    }


}