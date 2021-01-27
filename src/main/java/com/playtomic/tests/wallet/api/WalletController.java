package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.domain.dto.ChargeDto;
import com.playtomic.tests.wallet.domain.dto.ErrorDto;
import com.playtomic.tests.wallet.domain.dto.WalletDto;
import com.playtomic.tests.wallet.domain.dto.WithdrawDto;
import com.playtomic.tests.wallet.ex.ErrorException;
import com.playtomic.tests.wallet.ex.ErrorType;
import com.playtomic.tests.wallet.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletDto> getByID(@PathVariable("id") String id) {
        return new ResponseEntity<>(walletService.getWalletById(id), HttpStatus.OK);
    }

    @PostMapping("/charge")
    public ResponseEntity<Void> charge(@RequestBody ChargeDto dto) {
        walletService.charge(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody WithdrawDto dto) {
        walletService.withdraw(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> error(Exception ex) {
        if (ex instanceof ErrorException) {
            ErrorType errorType = ((ErrorException) ex).getErrorType();
            if (errorType.equals(ErrorType.WALLET_NOT_FOUND)) {
                return new ResponseEntity<>(ErrorDto.fromErr((ErrorException) ex), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(ErrorDto.fromErr((ErrorException) ex), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(ErrorDto.fromEx(ex), HttpStatus.BAD_REQUEST);
        }
    }
}
