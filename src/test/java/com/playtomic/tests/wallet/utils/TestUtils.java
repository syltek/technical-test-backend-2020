package com.playtomic.tests.wallet.utils;

import com.playtomic.tests.wallet.domain.dto.ChargeDto;
import com.playtomic.tests.wallet.domain.dto.WalletDto;
import com.playtomic.tests.wallet.domain.dto.WithdrawDto;
import com.playtomic.tests.wallet.domain.entity.WalletEntity;
import com.playtomic.tests.wallet.domain.entity.WalletTransactionEntity;
import com.playtomic.tests.wallet.domain.enums.Currency;
import com.playtomic.tests.wallet.domain.enums.PaymentPlatform;
import com.playtomic.tests.wallet.domain.enums.TransactionType;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.repository.WalletTransactionRepository;

import java.math.BigDecimal;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
public class TestUtils {

    public static final String TEST_ID = "670407e4-0e13-4355-97f0-9eea48210e16";

    public static WalletDto getWalletDto() {
        return new WalletDto(TEST_ID, "test", BigDecimal.valueOf(111.22));
    }

    public static ChargeDto getChargeDto() {
        return new ChargeDto(TEST_ID, PaymentPlatform.STRIPE, BigDecimal.valueOf(111.22));
    }

    public static ChargeDto getSmallAmountChargeDto() {
        return new ChargeDto(TEST_ID, PaymentPlatform.STRIPE, BigDecimal.valueOf(1.22));
    }

    public static WithdrawDto getWithdrawDto() {
        return new WithdrawDto(TEST_ID, BigDecimal.valueOf(111.22));
    }

    public static WithdrawDto getBigAmountWithdrawDto() {
        return new WithdrawDto(TEST_ID, BigDecimal.valueOf(222.44));
    }

    public static WithdrawDto getWrongIdWithdrawDto() {
        return new WithdrawDto("111", BigDecimal.valueOf(222.44));
    }

    public static WalletEntity getWalletEntity() {
        return new WalletEntity(TEST_ID, "test");
    }

    public static void initData(WalletRepository walletRepository,
                                WalletTransactionRepository walletTransactionRepository) {
        System.out.println("###################Init Data###############################");
        WalletEntity wallet = getWalletEntity();
        walletRepository.save(wallet);
        WalletTransactionEntity transaction = new WalletTransactionEntity(
                TEST_ID,
                BigDecimal.valueOf(111.22),
                TransactionType.DEBIT,
                Currency.EUR,
                PaymentPlatform.STRIPE,
                wallet
        );
        walletTransactionRepository.save(transaction);
    }

    public static void destroyData(WalletRepository walletRepository,
                                WalletTransactionRepository walletTransactionRepository) {
        System.out.println("###################Tear Down Data###############################");
        walletTransactionRepository.deleteAll();
        walletRepository.deleteAll();
    }
}
