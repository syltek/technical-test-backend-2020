package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.data.dto.WalletDTO;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(path = "/wallet/")
public class WalletController {
    private final Logger log = LoggerFactory.getLogger(WalletController.class);

    final PaymentService paymentService;

    public WalletController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("{id}")
    private ResponseEntity<?> getWalletInformation(@PathVariable String id) throws PaymentServiceException {
        if (Objects.isNull(id) || id.isEmpty()) throw new PaymentServiceException("Identifier not specified!");
        WalletDTO wallet;
        try {

            wallet = paymentService.getInfo(id);
        } catch (PaymentServiceException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        log.info(wallet.toString());
        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @PostMapping("charge")
    private ResponseEntity<String> charge(@Valid @RequestBody WalletDTO walletDTO) {
        try {
            paymentService.charge(walletDTO.getBalance());
        } catch (PaymentServiceException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        log.info("You have been charged for your purchase!");
        return new ResponseEntity<>("You have been charged for your purchase!", HttpStatus.OK);
    }

    @PostMapping("recharge")
    private ResponseEntity<String> recharge(@Valid @RequestBody WalletDTO walletDTO) {
        try {
            paymentService.reCharge(walletDTO);
        } catch (PaymentServiceException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("You have successfully recharged your balance with " + walletDTO.getBalance(), HttpStatus.OK);
    }


    @RequestMapping("")
    void log() {
        log.info("Logging from /");
    }
}
