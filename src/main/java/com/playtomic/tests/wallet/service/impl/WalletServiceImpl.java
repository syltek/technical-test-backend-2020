package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.exception.WalletNotEnoughBalanceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.dto.WalletDto;
import com.playtomic.tests.wallet.model.dto.WalletReChargeReqDto;
import com.playtomic.tests.wallet.model.entity.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final PaymentService paymentService;

    public WalletServiceImpl(WalletRepository walletRepository, PaymentService paymentService) {
        this.walletRepository = walletRepository;
        this.paymentService = paymentService;
    }

    @Override
    public CompletableFuture<WalletDto> findWalletById(Long walletId) {
        WalletDto walletDto = walletRepository.findById(walletId)
                .map(wallet -> WalletDto.builder().id(wallet.getId())
                        .balance(wallet.getBalance()).currency(String.valueOf(wallet.getCurrency())).build())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not exists"));

        return CompletableFuture.completedFuture(walletDto);
    }

    @Override
    public CompletableFuture<WalletDto> chargeWallet(WalletDto walletDto) {
        Wallet wallet = walletRepository.findById(walletDto.getId()).orElseThrow(() -> new WalletNotFoundException("Wallet not Exist"));

        if (wallet.getBalance().compareTo(walletDto.getBalance()) < 0 ||
                !wallet.getCurrency().name().equals(walletDto.getCurrency()))
            throw new WalletNotEnoughBalanceException("Not enough balance or Currency not supported");

        wallet.setBalance(wallet.getBalance().subtract(walletDto.getBalance()));
        walletRepository.save(wallet);

        return CompletableFuture.completedFuture(WalletDto.builder().balance(wallet.getBalance())
                .id(wallet.getId())
                .currency(wallet.getCurrency().name())
                .build());
    }

    @Override
    public CompletableFuture<WalletDto> reChargeWalletTopUp(WalletReChargeReqDto walletReChargeReqDto) throws PaymentServiceException {

        Wallet wallet = walletRepository.findById(walletReChargeReqDto.getId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not Exist"));

        paymentService.charge(walletReChargeReqDto.getChargeAmount(), walletReChargeReqDto.getPaymentChannel());
        wallet.setBalance(wallet.getBalance().add(walletReChargeReqDto.getChargeAmount()));
        walletRepository.save(wallet);

        return CompletableFuture.completedFuture(WalletDto.builder().balance(wallet.getBalance())
                .id(wallet.getId())
                .currency(wallet.getCurrency().name())
                .build());
    }

}
