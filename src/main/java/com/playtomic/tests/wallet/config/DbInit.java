package com.playtomic.tests.wallet.config;

import com.playtomic.tests.wallet.domain.entity.WalletEntity;
import com.playtomic.tests.wallet.domain.entity.WalletTransactionEntity;
import com.playtomic.tests.wallet.domain.enums.Currency;
import com.playtomic.tests.wallet.domain.enums.PaymentPlatform;
import com.playtomic.tests.wallet.domain.enums.TransactionType;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.repository.WalletTransactionRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.UUID;

/*init some data for debug*/
@Component
@Profile("develop")
public class DbInit {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public DbInit(WalletRepository walletRepository, WalletTransactionRepository walletTransactionRepository) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @PostConstruct
    public void initData() {
        System.out.println("###################Init Data###############################");
        WalletEntity wallet = new WalletEntity();
        wallet.setId(UUID.randomUUID().toString());
        wallet.setDescription("test");
        wallet = walletRepository.save(wallet);

        WalletTransactionEntity transaction = new WalletTransactionEntity(
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(100.00),
                TransactionType.DEBIT,
                Currency.EUR,
                PaymentPlatform.STRIPE,
                wallet
        );
        walletTransactionRepository.save(transaction);
    }

    @PreDestroy
    public void destroyData() {
        walletTransactionRepository.deleteAll();
        walletRepository.deleteAll();
    }

}
