package com.playtomic.tests.wallet.api.interfaces;


import com.playtomic.tests.wallet.api.model.WalletChargeDetails;
import com.playtomic.tests.wallet.api.model.WalletRead;
import com.playtomic.tests.wallet.api.model.WalletTopUpDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Tag(name = "wallet-api", description = "Api for doing wallet balance operations")
@Validated
public interface WalletOperationsAPI {

    @Operation(summary = "Get wallet information")
    @GetMapping(path = "wallet/{walletId}", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<WalletRead> getInfo(@PathVariable @NotNull Long walletId);

    @Operation(summary = "Charge an amount to a wallet")
    @PostMapping(path = "wallet/{walletId}/charge", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<WalletRead> charge(@PathVariable @NotNull Long walletId,
        @NotNull @Valid @RequestBody WalletChargeDetails chargeDetails);

    @Operation(summary = "TopUp a wallet")
    @PostMapping(path = "wallet/{walletId}/top-up", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<WalletRead> topUp(@PathVariable @NotNull Long walletId,
        @NotNull @Valid @RequestBody WalletTopUpDetails topUpDetails);

}
