package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.repository.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.WalletOperations;
import com.playtomic.tests.wallet.service.exception.WalletErrorCode;
import com.playtomic.tests.wallet.service.exception.WalletException;
import com.playtomic.tests.wallet.service.factory.PaymentServiceFactory;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

@Service
public class WalletService implements WalletOperations {

    private final WalletRepository walletRepository;
    private final PaymentServiceFactory paymentServiceFactory;

    private final Logger log = LoggerFactory.getLogger(WalletService.class);

    public WalletService(
        final WalletRepository walletRepository,
        final PaymentServiceFactory paymentServiceFactory) {
        this.walletRepository = walletRepository;
        this.paymentServiceFactory = paymentServiceFactory;
    }

    @Override
    public Mono<Wallet> getInfo(final Long walletId) {
        return walletRepository.findById(walletId)
            .switchIfEmpty(Mono.error(new WalletException(WalletErrorCode.WALLET_NOT_FOUND)));
    }

    @Override
    public Mono<Wallet> charge(final Long walletId, final BigDecimal amount,
        final String currency) {

        return getInfo(walletId)
            .doOnNext(wallet -> {

                if (wallet.getBalance().compareTo(amount) < 0) {
                    throw Exceptions.propagate(
                        new WalletException(WalletErrorCode.INSUFFICIENT_BALANCE));
                } else if (!wallet.getCurrency().equals(currency)) {
                    throw Exceptions.propagate(
                        new WalletException("This wallet uses " + wallet.getCurrency(),
                            WalletErrorCode.CURRENCY_NOT_SUPPORTED));
                }

            })
            .map(wallet -> {
                wallet.setBalance(wallet.getBalance().subtract(amount));
                return wallet;
            })
            .doOnNext(wallet -> log.info("Storing wallet after charge {}", wallet))
            .flatMap(walletRepository::save);
    }

    @Override
    public Mono<Wallet> topUp(final Long walletId, final BigDecimal amount, final String currency,
        final String paymentServiceName) {

        return Mono.just(Pair.of(amount, paymentServiceName))
            .doOnNext(pairAmountServiceName -> {

                try {
                    paymentServiceFactory.getPaymentService(pairAmountServiceName.getSecond())
                        .charge(pairAmountServiceName.getFirst());
                } catch (final PaymentServiceException e) {
                    throw Exceptions.propagate(e);
                }

            })
            .then(getInfo(walletId))
            .doOnNext(wallet -> {

                if (!wallet.getCurrency().equals(currency)) {
                    throw Exceptions.propagate(
                        new WalletException("This wallet uses " + wallet.getCurrency(),
                            WalletErrorCode.CURRENCY_NOT_SUPPORTED));
                }

            })
            .map(wallet -> {
                wallet.setBalance(wallet.getBalance().add(amount));
                return wallet;
            })
            .doOnNext(wallet -> log.info("Storing wallet after topUp {}", wallet))
            .flatMap(walletRepository::save);
    }
}
