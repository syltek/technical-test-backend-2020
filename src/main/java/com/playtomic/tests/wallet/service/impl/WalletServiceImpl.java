package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.domain.dto.ChargeDto;
import com.playtomic.tests.wallet.domain.dto.WalletDto;
import com.playtomic.tests.wallet.domain.dto.WithdrawDto;
import com.playtomic.tests.wallet.domain.entity.WalletEntity;
import com.playtomic.tests.wallet.domain.entity.WalletTransactionEntity;
import com.playtomic.tests.wallet.domain.enums.Currency;
import com.playtomic.tests.wallet.domain.enums.TransactionType;
import com.playtomic.tests.wallet.ex.ErrorException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.repository.WalletTransactionRepository;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static com.playtomic.tests.wallet.ex.ErrorType.*;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final ThirdPartyPaymentService thirdPartyPaymentService;


    public WalletServiceImpl(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository, ThirdPartyPaymentService thirdPartyPaymentService) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.thirdPartyPaymentService = thirdPartyPaymentService;
    }

    @Override
    public WalletDto getWalletById(final String id) {
        WalletEntity wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ErrorException(WALLET_NOT_FOUND, id));
        BigDecimal walletBalance = walletTransactionRepository.getBalance(id);
        return new WalletDto(
                wallet.getId(),
                wallet.getDescription(),
                walletBalance);
    }

    @Override
    public void charge(final ChargeDto dto) {
        final String id = validateAndGetWalletId(dto.getWalletId());

        thirdPartyPaymentService(dto.getAmount(), id);

        WalletEntity wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ErrorException(WALLET_NOT_FOUND, id));

        WalletTransactionEntity transaction = new WalletTransactionEntity(
                UUID.randomUUID().toString(),
                dto.getAmount(),
                TransactionType.DEBIT,
                Currency.EUR,
                dto.getPaymentPlatform(),
                wallet
        );
        walletTransactionRepository.save(transaction);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void withdraw(final WithdrawDto dto) {
        final String id = validateAndGetWalletId(dto.getWalletId());
        WalletEntity wallet = walletRepository.findById(id)
                .orElseThrow(() -> new ErrorException(WALLET_NOT_FOUND, id));

        validateBalance(dto.getAmount(), id);

        WalletTransactionEntity transaction = new WalletTransactionEntity(
                UUID.randomUUID().toString(),
                dto.getAmount().negate(),
                TransactionType.CREDIT,
                Currency.EUR,
                null,
                wallet
        );
        walletTransactionRepository.save(transaction);
    }

    private void validateBalance(final BigDecimal amount, final String id) {
        BigDecimal walletBalance = walletTransactionRepository.getBalance(id);
        if (walletBalance.compareTo(amount) < 0) {
            throw new ErrorException(NOT_ENOUGH_MONEY, id);
        }
    }

    private void thirdPartyPaymentService(final BigDecimal amount, final String id) {
        try {
            thirdPartyPaymentService.charge(amount);
        } catch (PaymentServiceException e) {
            throw new ErrorException(PAYMENT_SERVICE_ERROR, id);
        }
    }

    private String validateAndGetWalletId(final String id) {
        try {
             return UUID.fromString(id).toString();
        } catch (Exception e) {
            throw new ErrorException(INVALID_UUID, id);
        }
    }

}
