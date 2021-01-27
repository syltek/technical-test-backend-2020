package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.domain.dto.ChargeDto;
import com.playtomic.tests.wallet.domain.dto.WalletDto;
import com.playtomic.tests.wallet.domain.dto.WithdrawDto;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
public interface WalletService {

    WalletDto getWalletById(String id);

    void charge(ChargeDto dto);

    void withdraw(WithdrawDto dto);
}
