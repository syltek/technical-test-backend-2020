package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.dto.Operation;
import com.playtomic.tests.wallet.domain.dto.Transaction;
import com.playtomic.tests.wallet.repository.WalletRepository;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.BinaryOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

  private final WalletRepository walletRepository;
  private final PaymentService paymentService;

  public WalletService(final WalletRepository walletRepository,
      final PaymentService paymentService) {
    this.walletRepository = walletRepository;
    this.paymentService = paymentService;
  }

  public void updateWalletBalance(final Transaction transaction) throws PaymentServiceException {
    final BigDecimal amount = transaction.getAmount();
    final Operation operationValue = transaction.getOperation();
    final Long walletId = transaction.getWalletId();
    logger.debug(
        "Updating balance as a " + operationValue + " for the amount of " + amount);
    final Optional<Wallet> walletOptional = this.walletRepository
        .findById(walletId);

    if (!walletOptional.isPresent()) {
      final String msg = "Wallet with id: " + walletId + " was not found";
      logger.error(msg);
      throw new WalletServiceException(404, msg);
    }

    final Wallet wallet = walletOptional.get();
    final BigDecimal balance = wallet.getBalance();
    if (operationValue.equals(Operation.RECHARGE)) {
      this.paymentService.charge(amount);
    }
    // NO PAYMENT SERVICE EXCEPTION, PROCEED

    final BinaryOperator<BigDecimal> operation = computeOperation(operationValue);
    final BigDecimal resultBalance = operation.apply(balance, amount);
    wallet.setBalance(resultBalance);
  }

  private static BinaryOperator<BigDecimal> computeOperation(final Operation operation) {
    if (operation.equals(Operation.PAYMENT)) {
      return BigDecimal::subtract;
    } else {
      // REFUND OR RECHARGE
      return BigDecimal::add;
    }
  }
}
