package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.data.dto.WalletDTO;

import java.math.BigDecimal;

public interface PaymentService {
    void charge(BigDecimal amount) throws PaymentServiceException;

    WalletDTO getInfo(String id) throws PaymentServiceException;

    void reCharge(WalletDTO wallet) throws PaymentServiceException;
}
