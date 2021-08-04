package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.dto.WalletDTO;
import com.playtomic.tests.wallet.dto.WalletTransactionDTO;
import com.playtomic.tests.wallet.exceptions.NotEnoughMoneyInWalletException;
import com.playtomic.tests.wallet.exceptions.PaymentServiceException;
import com.playtomic.tests.wallet.exceptions.WalletAlreadyExistsException;
import com.playtomic.tests.wallet.exceptions.WalletNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WalletService {

  Optional<WalletDTO.WalletRes> findById(String id) throws WalletNotFoundException;

  Optional<List<WalletDTO.WalletRes>> findAll();

  Optional<WalletDTO.WalletRes> pay(String id, BigDecimal bigDecimal)
      throws WalletNotFoundException, NotEnoughMoneyInWalletException;

  Optional<WalletDTO.WalletRes> recharge(String id, BigDecimal bigDecimal)
      throws WalletNotFoundException, PaymentServiceException;

  Optional<WalletDTO.WalletRes> create(WalletDTO.WalletReq walletReq)
      throws WalletAlreadyExistsException;

  boolean delete(String id) throws WalletNotFoundException;

  Optional<List<WalletTransactionDTO.WalletTransactionRes>> findAllTransactionsForWalletId(String id);
}
