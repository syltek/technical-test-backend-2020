package com.playtomic.tests.wallet.service.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class WalletException extends RuntimeException {

    private WalletErrorCode errorCode;

    public WalletException(final WalletErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }


    public WalletException(final String message,
        final WalletErrorCode errorCode) {
        super(errorCode.getDescription() + ": " + message);
        this.errorCode = errorCode;
    }
}
