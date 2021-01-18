package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.repository.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.exception.PaymentServiceErrorCode;
import com.playtomic.tests.wallet.service.exception.WalletErrorCode;
import com.playtomic.tests.wallet.service.exception.WalletException;
import com.playtomic.tests.wallet.service.factory.PaymentServiceFactory;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@Import({
    WalletRepository.class,
    PaymentServiceFactory.class,
    WalletService.class,
    ThirdPartyPaymentService.class
})
class WalletServiceTest {

    @MockBean
    private WalletRepository walletRepository;

    @Autowired
    private WalletService walletService;

    @Test
    void getInfo_walletNotFound_walletException() {

        final Long walletId = 1L;

        Mockito.when(walletRepository.findById(walletId)).thenReturn(Mono.empty());

        StepVerifier.create(walletService.getInfo(walletId))
            .verifyErrorSatisfies(throwable -> {
                Assertions.assertEquals(WalletException.class, throwable.getClass());
                Assertions.assertEquals(WalletErrorCode.WALLET_NOT_FOUND.getDescription(),
                    throwable.getMessage());
                Assertions.assertEquals(WalletErrorCode.WALLET_NOT_FOUND,
                    ((WalletException) throwable).getErrorCode());

            });

        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
    }

    @Test
    void getInfo_walletExists_success() {

        final Long walletId = 1L;
        final BigDecimal amount = BigDecimal.valueOf(200);
        final String currency = "EUR";

        Mockito.when(walletRepository.findById(walletId))
            .thenReturn(Mono.just(
                Wallet.builder().id(walletId).balance(amount).currency(currency).build()));

        StepVerifier.create(walletService.getInfo(walletId))
            .expectNextMatches(
                wallet ->
                    wallet.getId().equals(walletId)
                        && wallet.getBalance().equals(amount)
                        && wallet.getCurrency().equals(currency))
            .verifyComplete();

        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
    }


    @Test
    void charge_insufficientBalance_walletException() {

        final Long walletId = 1L;
        final BigDecimal amountToCharge = BigDecimal.valueOf(2000);
        final BigDecimal amountInWallet = BigDecimal.valueOf(200);
        final String currency = "EUR";

        final Wallet wallet = Wallet.builder().id(walletId).balance(amountInWallet)
            .currency(currency).build();

        Mockito.when(walletRepository.findById(walletId))
            .thenReturn(Mono.just(wallet));

        StepVerifier.create(walletService.charge(walletId, amountToCharge, currency))
            .verifyErrorSatisfies(throwable -> {
                Assertions.assertEquals(WalletException.class, throwable.getClass());
                Assertions.assertEquals(WalletErrorCode.INSUFFICIENT_BALANCE.getDescription(),
                    throwable.getMessage());
                Assertions.assertEquals(WalletErrorCode.INSUFFICIENT_BALANCE,
                    ((WalletException) throwable).getErrorCode());

            });

        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
    }

    @Test
    void charge_incompatibleCurrency_walletException() {

        final Long walletId = 1L;
        final BigDecimal amountToCharge = BigDecimal.valueOf(20);
        final BigDecimal amountInWallet = BigDecimal.valueOf(200);
        final String currency = "EUR";

        final Wallet wallet = Wallet.builder().id(walletId).balance(amountInWallet)
            .currency(currency).build();

        Mockito.when(walletRepository.findById(walletId))
            .thenReturn(Mono.just(wallet));

        StepVerifier.create(walletService.charge(walletId, amountToCharge, "USD"))
            .verifyErrorSatisfies(throwable -> {
                Assertions.assertEquals(WalletException.class, throwable.getClass());
                Assertions.assertEquals(
                    WalletErrorCode.CURRENCY_NOT_SUPPORTED.getDescription() + ": This wallet uses "
                        + currency,
                    throwable.getMessage());
                Assertions.assertEquals(WalletErrorCode.CURRENCY_NOT_SUPPORTED,
                    ((WalletException) throwable).getErrorCode());

            });

        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);

    }

    @Test
    void charge_amountAndCurrencyOk_success() {

        final Long walletId = 1L;
        final BigDecimal amountToCharge = BigDecimal.valueOf(20);
        final BigDecimal amountInWallet = BigDecimal.valueOf(200);
        final String currency = "EUR";

        final Wallet walletMock = Wallet.builder().id(walletId).balance(amountInWallet)
            .currency(currency).build();

        Mockito.when(walletRepository.findById(walletId))
            .thenReturn(Mono.just(walletMock));

        Mockito.when(walletRepository.save(walletMock))
            .thenReturn(Mono.just(walletMock));

        StepVerifier.create(walletService.charge(walletId, amountToCharge, currency))
            .expectNextMatches(
                wallet ->
                    wallet.getId().equals(walletId)
                        && wallet.getBalance().equals(amountInWallet.subtract(amountToCharge))
                        && wallet.getCurrency().equals(currency))
            .verifyComplete();

        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
        Mockito.verify(walletRepository, Mockito.times(1)).save(walletMock);

    }


    @Test
    void topUp_paymentServiceNotAvailable_paymentException() {

        final Long walletId = 1L;
        final BigDecimal amountToTopUp = BigDecimal.valueOf(20);
        final BigDecimal amountInWallet = BigDecimal.valueOf(200);
        final String currency = "EUR";
        final String paymentService = "PayPal";

        final Wallet walletMock = Wallet.builder().id(walletId).balance(amountInWallet)
            .currency(currency).build();

        Mockito.when(walletRepository.findById(walletId))
            .thenReturn(Mono.just(walletMock));

        StepVerifier.create(walletService.topUp(walletId, amountToTopUp, currency, paymentService))
            .verifyErrorSatisfies(throwable -> {
                Assertions.assertEquals(PaymentServiceException.class, throwable.getClass());
                Assertions
                    .assertEquals(PaymentServiceErrorCode.SERVICE_NOT_AVAILABLE.getDescription(),
                        throwable.getMessage());
                Assertions.assertEquals(PaymentServiceErrorCode.SERVICE_NOT_AVAILABLE,
                    ((PaymentServiceException) throwable).getErrorCode());

            });

        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);

    }

    @Test
    void topUp_paymentChargeError_paymentException() {

        final Long walletId = 1L;
        final BigDecimal amountToTopUp = BigDecimal.valueOf(5);
        final BigDecimal amountInWallet = BigDecimal.valueOf(200);
        final String currency = "EUR";
        final String paymentService = "thirdPartyPaymentService";
        final BigDecimal threshold = BigDecimal.valueOf(10);

        final Wallet walletMock = Wallet.builder().id(walletId).balance(amountInWallet)
            .currency(currency).build();

        Mockito.when(walletRepository.findById(walletId))
            .thenReturn(Mono.just(walletMock));

        StepVerifier.create(walletService.topUp(walletId, amountToTopUp, currency, paymentService))
            .verifyErrorSatisfies(throwable -> {
                Assertions.assertEquals(PaymentServiceException.class, throwable.getClass());
                Assertions
                    .assertEquals(PaymentServiceErrorCode.INSUFFICIENT_AMOUNT.getDescription() +
                            ": The minimum amount to charge in " + paymentService + " service is "
                            + threshold,
                        throwable.getMessage());
                Assertions.assertEquals(PaymentServiceErrorCode.INSUFFICIENT_AMOUNT,
                    ((PaymentServiceException) throwable).getErrorCode());

            });

        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);

    }

    @Test
    void topUp_incompatibleCurrency_walletException() {

        final Long walletId = 1L;
        final BigDecimal amountToTopUp = BigDecimal.valueOf(20);
        final BigDecimal amountInWallet = BigDecimal.valueOf(200);
        final String currency = "EUR";
        final String paymentService = "thirdPartyPaymentService";

        final Wallet wallet = Wallet.builder().id(walletId).balance(amountInWallet)
            .currency(currency).build();

        Mockito.when(walletRepository.findById(walletId))
            .thenReturn(Mono.just(wallet));

        StepVerifier.create(walletService.topUp(walletId, amountToTopUp, "USD", paymentService))
            .verifyErrorSatisfies(throwable -> {
                Assertions.assertEquals(WalletException.class, throwable.getClass());
                Assertions.assertEquals(
                    WalletErrorCode.CURRENCY_NOT_SUPPORTED.getDescription() + ": This wallet uses "
                        + currency,
                    throwable.getMessage());
                Assertions.assertEquals(WalletErrorCode.CURRENCY_NOT_SUPPORTED,
                    ((WalletException) throwable).getErrorCode());

            });

        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
    }

    @Test
    void topUp_amountCurrencyAndPaymentServiceOk_success() {

        final Long walletId = 1L;
        final BigDecimal amountToTopUp = BigDecimal.valueOf(20);
        final BigDecimal amountInWallet = BigDecimal.valueOf(200);
        final String currency = "EUR";
        final String paymentService = "thirdPartyPaymentService";

        final Wallet walletMock = Wallet.builder().id(walletId).balance(amountInWallet)
            .currency(currency).build();

        Mockito.when(walletRepository.findById(walletId))
            .thenReturn(Mono.just(walletMock));

        Mockito.when(walletRepository.save(walletMock))
            .thenReturn(Mono.just(walletMock));

        StepVerifier.create(walletService.topUp(walletId, amountToTopUp, currency, paymentService))
            .expectNextMatches(
                wallet ->
                    wallet.getId().equals(walletId)
                        && wallet.getBalance().equals(amountInWallet.add(amountToTopUp))
                        && wallet.getCurrency().equals(currency))
            .verifyComplete();

        Mockito.verify(walletRepository, Mockito.times(1)).findById(walletId);
        Mockito.verify(walletRepository, Mockito.times(1)).save(walletMock);

    }

}
