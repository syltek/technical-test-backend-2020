package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletRepository walletRepository;

    public WalletController(final WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping("/wallet")
    private ResponseEntity<Wallet> getWalletById(@RequestParam final Long id) {
        final Optional<Wallet> walletOptional = this.walletRepository.findById(id);
        if (walletOptional.isPresent()) {
            return ResponseEntity.ok(walletOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
