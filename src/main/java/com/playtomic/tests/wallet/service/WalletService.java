package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.dto.Operation;
import com.playtomic.tests.wallet.domain.dto.Transaction;
import com.playtomic.tests.wallet.repository.WalletRepository;
import java.util.Optional;
import java.util.function.BinaryOperator;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  private final WalletRepository walletRepository;


  public WalletService(final WalletRepository walletRepository) {
    this.walletRepository = walletRepository;
  }

  public void updateWalletBalance(final Transaction transaction) {
    final Optional<Wallet> walletOptional = this.walletRepository
        .findById(transaction.getWalletId());
    if (!walletOptional.isPresent()) {
      throw new WalletServiceException(404,
          "Wallet with id: " + transaction.getWalletId() + " was not found");
    }
    final Wallet wallet = walletOptional.get();
    final Double balance = wallet.getBalance();
    final Double amount = transaction.getAmount();
    final BinaryOperator<Double> operation = computeOperation(transaction.getOperation());
    final Double resultBalance = operation.apply(balance, amount);
    wallet.setBalance(resultBalance);
  }

  private static BinaryOperator<Double> subtractFromBalance = (balance, amount) -> Double
      .sum(balance, amount * -1);
  private static BinaryOperator<Double> addToBalance = Double::sum;

  private static BinaryOperator<Double> computeOperation(final Operation operation) {
    if (operation.equals(Operation.PAYMENT)) {
      return subtractFromBalance;
    } else {
      return addToBalance;
    }
  }
}
