package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.adapter.WalletAdapter;
import com.playtomic.tests.wallet.dto.WalletDTO;
import com.playtomic.tests.wallet.dto.WalletTransactionDTO;
import com.playtomic.tests.wallet.exceptions.NotEnoughMoneyInWalletException;
import com.playtomic.tests.wallet.exceptions.PaymentServiceException;
import com.playtomic.tests.wallet.exceptions.WalletAlreadyExistsException;
import com.playtomic.tests.wallet.exceptions.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.WalletTransactions;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.repository.WalletTransactionsRepository;
import com.playtomic.tests.wallet.service.WalletService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class WalletServiceImpl implements WalletService {

  private final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

  @Autowired private WalletRepository walletRepository;

  @Autowired private WalletTransactionsRepository walletTransactionsRepository;

  @Autowired private ThirdPartyPaymentService thirdPartyPaymentService;

  @Override
  public Optional<WalletDTO.WalletRes> findById(String id) throws WalletNotFoundException {
    Optional<Wallet> wallet = walletRepository.findById(id);

    if (wallet.isEmpty()) {
      throw new WalletNotFoundException(String.format("Wallet with id: %s not found", id));
    }

    return Optional.of(WalletAdapter.ToWalletRes(wallet.get()));
  }

  @Override
  public Optional<List<WalletDTO.WalletRes>> findAll() {
    List<WalletDTO.WalletRes> walletResList =
        walletRepository.findAll().stream()
            .map(WalletAdapter::ToWalletRes)
            .collect(Collectors.toList());

    if (walletResList.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(walletResList);
  }

  @Override
  public Optional<List<WalletTransactionDTO.WalletTransactionRes>> findAllTransactionsForWalletId(
      String walletId) {
    List<WalletTransactionDTO.WalletTransactionRes> walletTraResList =
        walletTransactionsRepository.findByWalletId(walletId).stream()
            .map(
                walletTransaction ->
                    WalletTransactionDTO.WalletTransactionRes.builder()
                        .walletId(walletTransaction.getWalletId())
                        .amount(walletTransaction.getAmount())
                        .id(walletTransaction.getId())
                        .updatedAt(walletTransaction.getUpdatedAt())
                        .build())
            .collect(Collectors.toList());

    if (walletTraResList.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(walletTraResList);
  }

  @Override
  public Optional<WalletDTO.WalletRes> pay(String id, BigDecimal amount)
      throws NotEnoughMoneyInWalletException, WalletNotFoundException {
    Optional<WalletDTO.WalletRes> wallet = findById(id);

    if (wallet.isEmpty()) {
      throw new WalletNotFoundException(String.format("Wallet with id: %s not found", id));
    }

    if (wallet.get().getBalance().compareTo(amount) < 0) {
      throw new NotEnoughMoneyInWalletException(
          String.format("Low balance, compared to amount to pay: %s ", amount));
    }

    wallet.get().setBalance(wallet.get().getBalance().subtract(amount));

    Wallet actualWallet = walletRepository.save(WalletAdapter.ToWallet(wallet.get()));
    walletTransactionsRepository.save(
        WalletTransactions.builder()
            .amount(amount)
            .walletId(actualWallet.getId())
            .updatedAt(java.lang.System.currentTimeMillis())
            .build());
    logger.info(
        "Payed amount of: "
            + amount
            + " for wallet: "
            + wallet.get().getId()
            + " and currency: "
            + wallet.get().getCurrency());

    return Optional.of(WalletAdapter.ToWalletRes(actualWallet));
  }

  @Override
  public Optional<WalletDTO.WalletRes> recharge(String id, BigDecimal amount)
      throws WalletNotFoundException, PaymentServiceException {
    Optional<WalletDTO.WalletRes> maybeWallet = findById(id);

    // Early exit if wallet is not present.
    if (maybeWallet.isEmpty()) {
      throw new WalletNotFoundException(String.format("Wallet with id: %s not found", id));
    }

    thirdPartyPaymentService.charge(amount);
    logger.warn(
        "Charged the Wallet from Third Party Payment Provider with: "
            + amount
            + " amount in "
            + maybeWallet.get().getCurrency());

    WalletDTO.WalletRes actualWallet = maybeWallet.get();
    actualWallet.setBalance(actualWallet.getBalance().add(amount));
    // Save is actually doing upsert for us so this is OK
    Wallet updatedWallet = walletRepository.save(WalletAdapter.ToWallet(actualWallet));
    return Optional.of(WalletAdapter.ToWalletRes(updatedWallet));
  }

  @Override
  public Optional<WalletDTO.WalletRes> create(WalletDTO.WalletReq walletReq)
      throws WalletAlreadyExistsException {
    Optional<Wallet> maybeWallet = walletRepository.findById(walletReq.getId());

    if (maybeWallet.isPresent()) {
      throw new WalletAlreadyExistsException(
          String.format("Wallet with id: %s already exist", walletReq.getId()));
    }

    Wallet createWallet = walletRepository.save(WalletAdapter.ToWallet(walletReq));
    logger.info("Created wallet with id: " + createWallet.getId());

    return Optional.of(WalletAdapter.ToWalletRes(createWallet));
  }

  @Override
  public boolean delete(String walletId) throws WalletNotFoundException {
    Optional<Wallet> maybeWallet = walletRepository.findById(walletId);
    if (maybeWallet.isEmpty()) {
      throw new WalletNotFoundException(String.format("No wallet found for id: %s", walletId));
    }
    return true;
  }
}
