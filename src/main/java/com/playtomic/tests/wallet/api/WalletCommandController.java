package com.playtomic.tests.wallet.api;

import java.net.URI;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.dto.WalletChargeDTO;
import com.playtomic.tests.wallet.service.WalletCommandService;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletCommandController {
    private Logger log = LoggerFactory.getLogger(WalletCommandController.class);

    @Autowired
    private WalletCommandService service;
    
    @PostMapping
	public ResponseEntity<?> createWallet(
			@Valid @RequestBody WalletChargeDTO wallet) {
		log.info(" Create wallet ");
		
		Wallet savedWallet = service.createWallet(convertToEntity(wallet));
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedWallet.getId())
				.toUri();
		
		return ResponseEntity
				.created(location)
				.build();
	}
    
    @PutMapping("/pay")
	public ResponseEntity<?> pay(
			@Valid @RequestBody WalletChargeDTO wallet) {
		log.info(" Payment wallet with id = " + wallet.getId());
		
		service.payment(wallet.getId(), wallet.getAmount());
		
		return ResponseEntity.noContent().build();
	}
    
    @PutMapping("/charge")
	public ResponseEntity<?> charge(
			@Valid @RequestBody WalletChargeDTO wallet) {
		log.info(" Charge wallet with id = " + wallet.getId());
		
		service.charge(wallet.getId(), wallet.getAmount());
		
		return ResponseEntity.noContent().build();
	}
    
    private Wallet convertToEntity(WalletChargeDTO wallet) {
    	return new Wallet (wallet.getId(), wallet.getAmount());
    }
}
