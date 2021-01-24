package com.playtomic.tests.wallet.config;

import com.playtomic.tests.wallet.model.entity.Wallet;
import com.playtomic.tests.wallet.model.enums.CurrencyTypes;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    private final WalletRepository walletRepository;

    public DataInitializer(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Bean
    CommandLineRunner runner() {
        return args ->
                walletRepository.save(
                        Wallet.builder()
                                .balance(BigDecimal.valueOf(25))
                                .currency(CurrencyTypes.EUR).build()
                );
    }

}
