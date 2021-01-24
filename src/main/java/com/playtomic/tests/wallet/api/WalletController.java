package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.model.dto.WalletDto;
import com.playtomic.tests.wallet.model.dto.WalletReChargeReqDto;
import com.playtomic.tests.wallet.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    private WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping(path = "/{walletId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Async api for getting wallet info",
            summary = "Get wallet info")
    private CompletableFuture<ResponseEntity> getInfo(@NotNull @PathVariable final Long walletId) {
        log.info("Scheduling threads start with id {} ", walletId);
        return walletService.findWalletById(walletId).thenApply(ResponseEntity::ok);
    }

    @PostMapping(path = "/wallet-charge", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Async api for charging specified wallet",
            summary = "Charge a wallet")
    private CompletableFuture<ResponseEntity> chargeWallet(@Valid @NotNull @RequestBody WalletDto walletDto) {
        return walletService.chargeWallet(walletDto).thenApply(ResponseEntity::ok);
    }

    @PostMapping(path = "/wallet-recharge", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Async api for reCharging specified wallet with third party payment systems",
            summary = "Recharge a wallet with third party payment system")
    private CompletableFuture<ResponseEntity> reChargeWalletTopUp(@Valid @NotNull @RequestBody WalletReChargeReqDto walletReChargeReqDto) {
        return walletService.reChargeWalletTopUp(walletReChargeReqDto).thenApply(ResponseEntity::ok);
    }
}
