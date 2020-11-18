package com.playtomic.tests.wallet.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.playtomic.tests.wallet.dto.Payment;
import com.playtomic.tests.wallet.dto.Wallet;
import com.playtomic.tests.wallet.exception.PaymentException;
import com.playtomic.tests.wallet.exception.PaymentServiceException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.service.WalletService;

@Validated
@RestController
@RequestMapping(path = "/v{version}")
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);
    
    @Autowired
    private WalletService walletService;

    @GetMapping(path="/wallet/{uniqueId}")
	public ResponseEntity<Wallet> getWalletById(@PathVariable(name="version") final Integer version, @PathVariable(name="uniqueId") final String uniqueId) throws WalletNotFoundException {
    	log.info("Recovering wallet with ID: "+uniqueId);
    	return new ResponseEntity<>(walletService.findById(uniqueId), HttpStatus.OK);
	}

	@PostMapping(path = "/make-payment", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Wallet> makePayment(@PathVariable(name="version") final Integer version, 
			@RequestBody Payment bodyData) throws WalletNotFoundException, PaymentException {
		log.info("Making Payment of " + bodyData.getAmount().toString() + " from wallet with ID: "+ bodyData.getId());
		return new ResponseEntity<>(walletService.makePayment(bodyData.getId(), bodyData.getAmount()), HttpStatus.OK);
	}
	
	@PostMapping(path = "/recharge-wallet", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Wallet> rechargeWallet(@PathVariable(name="version") final Integer version, 
			@RequestBody Payment bodyData) throws WalletNotFoundException, PaymentException, PaymentServiceException {
		log.info("Making Recharge of " + bodyData.getAmount().toString() + " from wallet with ID: "+ bodyData.getId());
		return new ResponseEntity<>(walletService.rechargeWallet(bodyData.getId(), bodyData.getAmount()), HttpStatus.OK);
	}
	
}
