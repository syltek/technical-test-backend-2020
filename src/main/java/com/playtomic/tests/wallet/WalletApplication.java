package com.playtomic.tests.wallet;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.playtomic.tests.wallet.model.WalletDAO;
import com.playtomic.tests.wallet.repository.WalletRepository;

@SpringBootApplication
public class WalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}
	
	/*Initialize the application with some default wallets*/
	@Bean
	public CommandLineRunner setup(WalletRepository walletRepository) {
		return (args) -> {
			walletRepository.save(new WalletDAO("fe4b59f1-b790-4603-8e5b-148a2254efab", new BigDecimal(100.00)));
			walletRepository.save(new WalletDAO("fe4b59f1-b790-4603-8e5b-148a2254efac", new BigDecimal(100.00)));
			walletRepository.save(new WalletDAO("fe4b59f1-b790-4603-8e5b-148a2254efad", new BigDecimal(100.00)));
		};
	}
}
