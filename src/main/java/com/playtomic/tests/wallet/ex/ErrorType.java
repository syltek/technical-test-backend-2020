package com.playtomic.tests.wallet.ex;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@AllArgsConstructor
public enum ErrorType {
    INVALID_JSON_TYPE("00.00", "Invalid json type:"),
    WALLET_NOT_FOUND("00.01", "Wallet not found by ID:"),
    INVALID_UUID("00.02", "Invalid UUID:"),
    PAYMENT_SERVICE_ERROR("00.03", "Third-party payment service error for wallet:"),
    NOT_ENOUGH_MONEY("00.04", "Not enough money in the wallet:");
    @Getter
    private final String code;
    @Getter
    private final String message;
}
