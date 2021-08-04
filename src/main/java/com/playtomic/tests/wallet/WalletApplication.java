package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class WalletApplication {

  public static void main(String[] args) {
    SpringApplication.run(WalletApplication.class, args);
  }

  @Bean
  public CommandLineRunner loadData(WalletRepository walletRepository) {
    return (args) -> {
      walletRepository.save(
          Wallet.builder()
              .id("7a6ffed9-4252-427e-af7d-3dcaaf2db2df")
              .balance(new BigDecimal("50.00"))
              .currency("EUR")
              .build());
      walletRepository.save(
          Wallet.builder()
              .id("7a6ffed9-4252-427e-af7d-3dcaaf2db1eg")
              .balance(new BigDecimal("69.00"))
              .currency("EUR")
              .build());
    };
  }
}
