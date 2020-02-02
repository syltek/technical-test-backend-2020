package com.playtomic.tests.wallet.api;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.model.dto.WalletQueryDTO;
import com.playtomic.tests.wallet.service.WalletQueryService;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletQueryController {
    private Logger log = LoggerFactory.getLogger(WalletQueryController.class);

    @Autowired
    private WalletQueryService service;
    
    @GetMapping
	public List<WalletQueryDTO> getAllWallets() {
		log.info(" Get all Wallets ");
		return service.searchAll()
				.stream()
				.map(
					(w) -> convertToDTO(w))
				.collect(Collectors.toList());
	}
    
    @GetMapping("/{id}")
	public WalletQueryDTO getWallet(@PathVariable Integer id) {
		log.info(" Get wallet id = " + id);
		return convertToDTO(service.searchById(id));
	}
    
    private WalletQueryDTO convertToDTO(Wallet wallet) {
    	return new WalletQueryDTO(wallet.getId(), wallet.getBalance());
    }
}
