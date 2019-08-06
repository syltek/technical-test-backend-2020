package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.domain.dto.Transaction;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.service.WalletService;
import com.playtomic.tests.wallet.service.WalletServiceException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletRepository walletRepository;
    private final WalletService walletService;

    public WalletController(final WalletRepository walletRepository, final WalletService walletService) {
        this.walletRepository = walletRepository;
        this.walletService = walletService;
    }

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping("/wallet")
    @ResponseBody
    private ResponseEntity<Wallet> getWalletById(@RequestParam final Long id) {
        final Optional<Wallet> walletOptional = this.walletRepository.findById(id);
        if (walletOptional.isPresent()) {
            return ResponseEntity.ok(walletOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/wallet")
    @ResponseBody
    private ResponseEntity updateWalletBalance(@RequestBody final Transaction transaction) {
        try {
            this.walletService.updateWalletBalance(transaction);
        } catch (WalletServiceException e) {
            if (e.getCode() == 404) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (PaymentServiceException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }

}
