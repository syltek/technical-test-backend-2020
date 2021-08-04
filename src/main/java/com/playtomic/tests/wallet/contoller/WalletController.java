package com.playtomic.tests.wallet.contoller;

import com.playtomic.tests.wallet.dto.WalletDTO;
import com.playtomic.tests.wallet.dto.WalletTransactionDTO;
import com.playtomic.tests.wallet.exceptions.NotEnoughMoneyInWalletException;
import com.playtomic.tests.wallet.exceptions.PaymentServiceException;
import com.playtomic.tests.wallet.exceptions.WalletAlreadyExistsException;
import com.playtomic.tests.wallet.exceptions.WalletNotFoundException;
import com.playtomic.tests.wallet.model.Payment;
import com.playtomic.tests.wallet.service.WalletService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/wallet")
@RestController
@AllArgsConstructor
@Api(tags = "Wallet")
public class WalletController {
  private final Logger logger = LoggerFactory.getLogger(WalletController.class);

  @Autowired private WalletService walletService;

  @GetMapping(
      path = "/{id}",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<WalletDTO.WalletRes> getById(@PathVariable(name = "id") final String id)
      throws WalletNotFoundException {
    logger.info("Fetching wallet with id: " + id);
    Optional<WalletDTO.WalletRes> walletOpt = walletService.findById(id);
    return walletOpt.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
  }

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<List<WalletDTO.WalletRes>> findAll() {
    logger.info("Fetching all wallets");
    Optional<List<WalletDTO.WalletRes>> walletsOpt = walletService.findAll();
    return walletsOpt.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
  }

  @GetMapping(
      path = "/transactions/{walletId}",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<List<WalletTransactionDTO.WalletTransactionRes>> findAllWalletTrans(
      @PathVariable(name = "walletId") final String walletId) {
    logger.info("Fetching all wallet transactions");
    Optional<List<WalletTransactionDTO.WalletTransactionRes>> walletsOpt =
        walletService.findAllTransactionsForWalletId(walletId);
    return walletsOpt.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
  }

  @PostMapping(path = "/pay", consumes = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseEntity<WalletDTO.WalletRes> pay(@RequestBody Payment payment)
      throws WalletNotFoundException, NotEnoughMoneyInWalletException {

    Optional<WalletDTO.WalletRes> pay = walletService.pay(payment.getId(), payment.getBalance());
    return pay.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
  }

  @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  public ResponseEntity<WalletDTO.WalletRes> create(@RequestBody WalletDTO.WalletReq walletReq)
      throws WalletAlreadyExistsException {
    Optional<WalletDTO.WalletRes> walletRes = walletService.create(walletReq);

    return walletRes.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @PostMapping(path = "/recharge", consumes = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody ResponseEntity<WalletDTO.WalletRes> recharge(@RequestBody Payment payment)
      throws WalletNotFoundException, PaymentServiceException {

    Optional<WalletDTO.WalletRes> recharge =
        walletService.recharge(payment.getId(), payment.getBalance());
    return recharge.map(ResponseEntity::ok).orElse(ResponseEntity.badRequest().build());
  }

  @DeleteMapping(
      path = "/{id}",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public boolean delete(@PathVariable String id) throws WalletNotFoundException {
    return walletService.delete(id);
  }
}
