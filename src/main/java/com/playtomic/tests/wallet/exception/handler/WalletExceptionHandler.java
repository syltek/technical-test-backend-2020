package com.playtomic.tests.wallet.exception.handler;

import com.playtomic.tests.wallet.exception.PaymentChannelNotFoundException;
import com.playtomic.tests.wallet.exception.WalletNotEnoughBalanceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.exception.dto.ErrorDto;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WalletExceptionHandler {

    /**
     *  HTTP status code NOT_FOUND (404)
     *
     * @param e Exception to be handled
     * @return {@link ErrorDto} containing the error message
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDto> walletNotFoundException(WalletNotFoundException e) {
        final ErrorDto errorDto = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    /**
     *  HTTP status code NOT_FOUND (403)
     *
     * @param e Forbidden operation if client does not have enough balance
     * @return {@link ErrorDto} containing the error message
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDto> walletNotEnoughBalanceException(WalletNotEnoughBalanceException e) {
        final ErrorDto errorDto = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    /**
     *  HTTP status code NOT_FOUND (404)
     *
     * @param e Third party payment service not supported
     * @return {@link ErrorDto} containing the error message
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDto> paymentServiceException(PaymentServiceException e) {
        final ErrorDto errorDto = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    /**
     *  HTTP status code NOT_FOUND (404)
     *
     * @param e Third party payment service not supported
     * @return {@link ErrorDto} containing the error message
     */
    @ExceptionHandler
    public ResponseEntity<ErrorDto> paymentChannelNotFoundException(PaymentChannelNotFoundException e) {
        final ErrorDto errorDto = new ErrorDto(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }
}
