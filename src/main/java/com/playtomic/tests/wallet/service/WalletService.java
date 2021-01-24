package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.model.dto.WalletDto;
import com.playtomic.tests.wallet.model.dto.WalletReChargeReqDto;

import java.util.concurrent.CompletableFuture;

public interface WalletService {

    CompletableFuture<WalletDto> findWalletById(final Long walletId);
    CompletableFuture<WalletDto> chargeWallet(final WalletDto walletDto);
    CompletableFuture<WalletDto> reChargeWalletTopUp(final WalletReChargeReqDto walletReChargeReqDto) throws PaymentServiceException;

}
