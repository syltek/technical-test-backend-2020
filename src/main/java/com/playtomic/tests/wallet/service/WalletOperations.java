package com.playtomic.tests.wallet.service;


import com.playtomic.tests.wallet.model.Wallet;
import java.math.BigDecimal;
import reactor.core.publisher.Mono;

public interface WalletOperations {

    Mono<Wallet> getInfo(Long walletId);

    Mono<Wallet> charge(Long walletId, BigDecimal amount, String currency);

    Mono<Wallet> topUp(Long walletId, BigDecimal amount, String currency,
        String paymentServiceName);

}
