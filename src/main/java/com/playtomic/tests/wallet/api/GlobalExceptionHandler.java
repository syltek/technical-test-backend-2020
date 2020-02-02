package com.playtomic.tests.wallet.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.playtomic.tests.wallet.exception.CustomApiErrorMessage;
import com.playtomic.tests.wallet.exception.WalletChargeException;
import com.playtomic.tests.wallet.exception.WalletFoundException;
import com.playtomic.tests.wallet.exception.WalletNotEnoughBalanceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(value = WalletNotFoundException.class)
	public ResponseEntity<CustomApiErrorMessage> handleWalletNotFoundException(WalletNotFoundException ex) {
		return createResponseEntity(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), null);
	}
	
	@ExceptionHandler(value = WalletFoundException.class)
	public ResponseEntity<CustomApiErrorMessage> handleWalletFoundException(WalletFoundException ex) {
		return createResponseEntity(HttpStatus.CONFLICT, ex.getLocalizedMessage(), null);
	}
	
	@ExceptionHandler(value = WalletNotEnoughBalanceException.class)
	public ResponseEntity<CustomApiErrorMessage> handleWalletNotEnoughBalanceException(WalletNotEnoughBalanceException ex) {
		return createResponseEntity(HttpStatus.PRECONDITION_FAILED, ex.getLocalizedMessage(), null);
	}
	
	@ExceptionHandler(value = WalletChargeException.class)
	public ResponseEntity<CustomApiErrorMessage> handleWalletChargeException(WalletChargeException ex) {
		return createResponseEntity(HttpStatus.PRECONDITION_FAILED, ex.getLocalizedMessage(), null);
	}
	
	@ExceptionHandler(value = InvalidFormatException.class)
	public ResponseEntity<CustomApiErrorMessage> handleInvalidFormatException(InvalidFormatException ex) {
		StringBuffer errorMessage = new StringBuffer("Parameter error: ");
		errorMessage.append(ex.getPath().get(0).getFieldName());
		errorMessage.append(", value: " + ex.getValue());
		
		return createResponseEntity(HttpStatus.BAD_REQUEST, errorMessage.toString(), ex.getLocalizedMessage());
	}
	
	private ResponseEntity<CustomApiErrorMessage> createResponseEntity(HttpStatus status, String message, String debugMessage) {
		logger.error(status.toString() + " - " + message + " - " + debugMessage);
		
		return ResponseEntity
				.status(status)
				.body(new CustomApiErrorMessage
						.CustomApiErrorMessageBuilder()
						.setStatus(status)
						.setError(message)
						.setDebugMessage(debugMessage)
						.build()
						);
	}
}
