package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.api.interfaces.WalletOperationsAPI;
import com.playtomic.tests.wallet.api.model.WalletChargeDetails;
import com.playtomic.tests.wallet.api.model.WalletRead;
import com.playtomic.tests.wallet.api.model.WalletTopUpDetails;
import com.playtomic.tests.wallet.service.WalletOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WalletController implements WalletOperationsAPI {

    private final WalletOperations walletOperations;
    private final Logger log = LoggerFactory.getLogger(WalletController.class);


    public WalletController(
        final WalletOperations walletOperations) {
        this.walletOperations = walletOperations;
    }

    @PostMapping(value = "/log")
    void log() {
        log.info("Logging from /log");
    }

    @Override
    public Mono<WalletRead> getInfo(final Long walletId) {

        log.info("Received request to get info for wallet with id {}", walletId);

        return walletOperations.getInfo(walletId).map(
            wallet -> WalletRead.builder().id(wallet.getId()).balance(wallet.getBalance())
                .currency(wallet.getCurrency()).build());
    }

    @Override
    public Mono<WalletRead> charge(final Long walletId,
        @RequestBody final WalletChargeDetails chargeDetails) {

        log.info("Received request to charge wallet id {} with details {}", walletId,
            chargeDetails);

        return walletOperations
            .charge(walletId, chargeDetails.getAmount(), chargeDetails.getCurrency())
            .map(
                wallet -> WalletRead.builder().id(wallet.getId()).balance(wallet.getBalance())
                    .currency(wallet.getCurrency())
                    .build());
    }

    @Override
    public Mono<WalletRead> topUp(final Long walletId,
        @RequestBody final WalletTopUpDetails topUpDetails) {

        log.info("Received request to topUp wallet id{} with details {}", walletId, topUpDetails);

        return walletOperations
            .topUp(walletId, topUpDetails.getAmount(), topUpDetails.getCurrency(),
                topUpDetails.getPaymentServiceName())
            .map(
                wallet -> WalletRead.builder().id(wallet.getId()).balance(wallet.getBalance())
                    .currency(wallet.getCurrency())
                    .build());
    }
}
