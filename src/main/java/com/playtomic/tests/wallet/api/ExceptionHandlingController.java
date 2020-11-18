package com.playtomic.tests.wallet.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.playtomic.tests.wallet.dto.ErrorResponse;
import com.playtomic.tests.wallet.exception.PaymentException;
import com.playtomic.tests.wallet.exception.PaymentServiceException;
import com.playtomic.tests.wallet.exception.WalletAlreadyExistsException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;

@ControllerAdvice
public class ExceptionHandlingController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exception(Exception exception) {
		logger.error("Unhandled exception in wallet service", exception);
		return new ResponseEntity<ErrorResponse>(new ErrorResponse("Unhandled exception in wallet service", exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ErrorResponse> exception(PaymentException exception) {
		logger.warn("Attempted payment with not enough balance available.", exception);
		return new ResponseEntity<ErrorResponse>(new ErrorResponse("Can not make payment, balance is not enough.", exception.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(PaymentServiceException.class)
	public ResponseEntity<ErrorResponse> exception(PaymentServiceException exception) {
		logger.warn("Attempted recharge of less than 5. Cannot process payment.", exception);
		return new ResponseEntity<ErrorResponse>(new ErrorResponse("Cannot process payment of less than 5€.", exception.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(WalletAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> exception(WalletAlreadyExistsException exception) {
		logger.warn("Attempted creation of existing wallet.", exception);
		return new ResponseEntity<ErrorResponse>(new ErrorResponse("This wallet already exists.", exception.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(WalletNotFoundException.class)
	public ResponseEntity<ErrorResponse> exception(WalletNotFoundException exception) {
		logger.warn("Attempted recovery of non existant wallet.", exception);
		return new ResponseEntity<ErrorResponse>(new ErrorResponse("The wallet requested does not exist.", exception.getMessage(), HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
	}
}