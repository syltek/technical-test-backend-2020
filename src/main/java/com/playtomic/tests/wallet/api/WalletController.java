package com.playtomic.tests.wallet.api;

import java.util.List;

import javax.xml.ws.ResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.playtomic.tests.wallet.json.WalletInfoReponse;
import com.playtomic.tests.wallet.json.WalletMoveMoney;
import com.playtomic.tests.wallet.service.IWalletService;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.store.Wallet;
import com.playtomic.tests.wallet.utils.Constants;

@RestController
public class WalletController {
	private Logger log = LoggerFactory.getLogger(WalletController.class);

	@Autowired
	IWalletService walletService;
	
	@Autowired
	PaymentService paymentService;

	@RequestMapping("/")
	void log() {
		log.info("Logging from /");
	}

	// CONSULTA TODOS LOS WALLET
	@GetMapping("/wallets")
	@ResponseBody
	private ResponseEntity<List<Wallet>> getAllWallets() {

		return new ResponseEntity<List<Wallet>>(walletService.getWallets(), HttpStatus.OK);

	}

	// INSERTAR NUEVO WALLET
	@PostMapping("/wallets")
	@ResponseWrapper
	private ResponseEntity<WalletInfoReponse> newUpdateWallet(@RequestBody Wallet wallet) {

		Wallet wConsult = walletService.findById(wallet.getIdWallet());
		WalletInfoReponse wInfoResponse = null;
		Wallet wCreatedUpdated = null;
		
		if (wConsult == null) {
			
			wCreatedUpdated = walletService.save(wallet);
			wInfoResponse = new WalletInfoReponse(wCreatedUpdated, Constants.WALLET_CREATED, "The wallet has been created");	
		}else {

			wallet = walletService.attControl(wallet,wConsult);
			wCreatedUpdated = walletService.save(wallet);
			wInfoResponse = new WalletInfoReponse(wCreatedUpdated, Constants.WALLET_UPDATED, "The wallet has been updated");	
		}

		return new ResponseEntity<>(wInfoResponse, HttpStatus.CREATED);
	}

	// CONSULTA WALLET
	@GetMapping("/wallets/{id}")
	private ResponseEntity<WalletInfoReponse> getWallet(@PathVariable("id") int id) {
		Wallet wConsult = walletService.findById(id);
		WalletInfoReponse wInfoResponse = null;
		HttpStatus status = null;
		
		if (wConsult == null) {
			
			wInfoResponse = new WalletInfoReponse(new Wallet(), Constants.WALLET_NOT_EXIST, "The wallet with id " + id + " not exist");	
			status = HttpStatus.NOT_FOUND;
		} else {
			
			wInfoResponse = new WalletInfoReponse(wConsult, Constants.WALLET_FOUND, "The wallet with id " + id + " found");	
			status = HttpStatus.OK;
		}

		return new ResponseEntity<>(wInfoResponse,status);
	}

	// RECARGA: Recargar dinero en ese bono a través de un servicio de pago de terceros.
	@PostMapping("/wallets/{id}/charge")
	private  ResponseEntity<WalletInfoReponse> chargeWallet(@PathVariable("id") int id, @RequestBody WalletMoveMoney wMoveMoney) {
		Wallet wConsult = walletService.findById(id);
		WalletInfoReponse wInfoResponse = null;
		
		try {
			if(wConsult!=null) {
				
					paymentService.charge(wMoveMoney.getAmount());
					Wallet upWallet = walletService.chargeWallet(wConsult, wMoveMoney.getAmount());
					wInfoResponse = new WalletInfoReponse(upWallet, Constants.PAYMENT_CHARGED, wMoveMoney.getAmount() + " CHARGED")	;		
			}else {
				
				wInfoResponse = new WalletInfoReponse(new Wallet(), Constants.ERROR_WALLET_NOT_FOUND, "There is no wallet with id: "+ id);	
			}
		} catch (PaymentServiceException e) {
			
			wInfoResponse = new WalletInfoReponse(new Wallet(), Constants.ERROR_MIN_PAYMENT, "Minimum payment 10€");
			return new ResponseEntity<>(wInfoResponse, HttpStatus.NOT_ACCEPTABLE);
		}
		
		return new ResponseEntity<>(wInfoResponse, HttpStatus.ACCEPTED);
	}

	// DEVOLUCION: Devolver saldo al monedero
	@PostMapping("/wallets/{id}/refund")
	private ResponseEntity<WalletInfoReponse> refundWallet(@PathVariable("id") int id, @RequestBody WalletMoveMoney wMoveMoney) {
		Wallet wConsult = walletService.findById(id);
		WalletInfoReponse wInfoResponse = null;
		
			if(wConsult!=null) {
				
				Wallet upWallet = walletService.refundWallet(wConsult, wMoveMoney.getAmount());
				wInfoResponse = new WalletInfoReponse(upWallet, Constants.PAYMENT_REFUNDED, wMoveMoney.getAmount() + " REFUNDED");
			}else {

				wInfoResponse = new WalletInfoReponse(new Wallet(), Constants.ERROR_WALLET_NOT_FOUND, "There is no wallet with id: "+ id);	
			}

		return new ResponseEntity<>(wInfoResponse, HttpStatus.ACCEPTED);
	}
	
	//COBRO: Descontar saldo del monedero
	@PostMapping("/wallets/{id}/payment")
	private ResponseEntity<WalletInfoReponse> paymentWallet(@PathVariable("id") int id, @RequestBody WalletMoveMoney wMoveMoney) {
		Wallet wConsult = walletService.findById(id);
		WalletInfoReponse wInfoResponse = null;
			
			if(wConsult!=null) {
				if(wMoveMoney.getAmount().compareTo(wConsult.getBalance()) <= 0) {
					
					Wallet upWallet = walletService.paymentWallet(wConsult, wMoveMoney.getAmount());
					wInfoResponse = new WalletInfoReponse(upWallet, Constants.PAYMENTED, wMoveMoney.getAmount() + " PAYMENTED");	
				}else {
					
					wInfoResponse = new WalletInfoReponse(wConsult, Constants.ERROR_NO_CREDIT, "Impossible charge, this wallet dont have credit");	
				}
			}else {
				
				wInfoResponse = new WalletInfoReponse(new Wallet(), Constants.ERROR_WALLET_NOT_FOUND, "There is no wallet with id: "+ id);	
			}
			
		return new ResponseEntity<>(wInfoResponse, HttpStatus.ACCEPTED);
	}
	
}
