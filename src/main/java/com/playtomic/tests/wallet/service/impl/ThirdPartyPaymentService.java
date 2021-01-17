package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.data.Wallet;
import com.playtomic.tests.wallet.data.dto.WalletDTO;
import com.playtomic.tests.wallet.repositories.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.PaymentServiceException;
import com.playtomic.tests.wallet.utils.WalletMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;


/**
 * A real implementation would call to a third party's payment service (such as Stripe, Paypal, Redsys...).
 * <p>
 * This is a dummy implementation which throws an error when trying to change less than 10€.
 */
@Service
public class ThirdPartyPaymentService implements PaymentService {
    private final Logger log = LoggerFactory.getLogger(ThirdPartyPaymentService.class);

    private final BigDecimal threshold = new BigDecimal(10);

    private final WalletRepository walletRepository;

    public ThirdPartyPaymentService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * I would change the method signature. Instead of sending a {@link BigDecimal} object i would send a {@link WalletDTO} object because i would need
     * an identifier to fetch the valid data from DB. I did not change it because the Tests were using a signature as that.
     *
     * @param amount
     * @throws PaymentServiceException
     */
    @Override
    public synchronized void charge(BigDecimal amount) throws PaymentServiceException {
        if (amount.compareTo(threshold) < 0) {
            throw new PaymentServiceException();
        }
        log.info("Requesting wallet with id: 1");
        Wallet wallet = walletRepository.findOne(1L);
        if (Objects.isNull(wallet)) {
            log.error("UserID is wrong. Please enter a correct User ID!");
            throw new PaymentServiceException("UserID is wrong. Please enter a correct User ID!");
        }

        if (wallet.getBalance().compareTo(amount) < 0) {
            log.error("Not enough money to make this purchase!!");
            throw new PaymentServiceException("Not enough money to make this purchase!!");
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
    }

    @Override
    public WalletDTO getInfo(String id) throws PaymentServiceException {
        log.info("Requesting wallet with id: {}", id);
        Wallet wallet = walletRepository.findOne(Long.valueOf(id));

        if (Objects.isNull(wallet)) {
            throw new PaymentServiceException("User not found!!");
        }
        return WalletMapper.walletToWalletDTOMapper(wallet);
    }

    @Override
    public void reCharge(WalletDTO wallet) throws PaymentServiceException {
        if (wallet.getBalance().compareTo(threshold) < 0) {
            throw new PaymentServiceException();
        }
        log.info("Requesting wallet with id: {}", wallet.getId());
        Wallet walletEntity = walletRepository.findOne(wallet.getId());
        if (Objects.isNull(walletEntity)) {
            log.error("UserID is wrong. Please enter a correct User ID!");
            throw new PaymentServiceException("UserID is wrong. Please enter a correct User ID!");
        }
        dummyMethodActingAsThirdPartyForRechargingYourBalance(walletEntity, wallet.getBalance());
        walletRepository.save(walletEntity);
    }

    private void dummyMethodActingAsThirdPartyForRechargingYourBalance(Wallet wallet, BigDecimal balance) {
        log.info("Reaching out to Global Payment Third Party for recharging your wallet for id = {}!", wallet.getId());
        BigDecimal decimal = wallet.getBalance().add(balance);
        wallet.setBalance(decimal);
        log.info("Wallet with the ID: {} was successfully recharged!", wallet.getId());
    }
}
