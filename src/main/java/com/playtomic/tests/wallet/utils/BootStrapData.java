package com.playtomic.tests.wallet.utils;

import com.playtomic.tests.wallet.data.Wallet;
import com.playtomic.tests.wallet.repositories.WalletRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BootStrapData implements CommandLineRunner {

    private final WalletRepository walletRepository;

    public BootStrapData(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Wallet wallet = new Wallet(1L,"Jasmin","Merusic", BigDecimal.valueOf(10000L));
        walletRepository.save(wallet);
        System.out.println("Bootstrap started!");
    }
}
