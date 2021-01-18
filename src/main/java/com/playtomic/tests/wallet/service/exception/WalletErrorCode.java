package com.playtomic.tests.wallet.service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WalletErrorCode {
    WALLET_NOT_FOUND("Wallet not found"),
    CURRENCY_NOT_SUPPORTED("Currency is not supported"),
    INSUFFICIENT_BALANCE("Insufficient balance in wallet");

    private final String description;


}
