package com.playtomic.tests.wallet;

import com.playtomic.tests.wallet.model.Wallet;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class WalletApplicationIT {

  private static final BigDecimal BALANCE_OF_FIVE = new BigDecimal("5.00");
  private static final BigDecimal BALANCE_OF_TEN = BALANCE_OF_FIVE.add(BALANCE_OF_FIVE);
  private static final BigDecimal BALANCE_OF_TWENTY = BALANCE_OF_TEN.add(BALANCE_OF_TEN);

  @Autowired private WalletRepository walletRepository;

  @Test
  @DisplayName("Get wallet")
  public void getWallet_success() {
    Wallet wallet = new Wallet(UUID.randomUUID().toString(), BALANCE_OF_TEN);
    walletRepository.save(wallet);

    Optional<Wallet> walletOpt = walletRepository.findById(wallet.getId());

    assertEquals(wallet.getId(), walletOpt.get().getId());
    assertEquals(wallet.getBalance(), walletOpt.get().getBalance());
    assertEquals(wallet.getCurrency(), walletOpt.get().getCurrency());
  }

  @Test
  @DisplayName("Get wallets")
  public void getAllWallets_success() {
    Wallet wallet1 = new Wallet(UUID.randomUUID().toString(), BALANCE_OF_TEN);
    Wallet wallet2 = new Wallet(UUID.randomUUID().toString(), BALANCE_OF_TWENTY);
    walletRepository.save(wallet1);
    walletRepository.save(wallet2);

    List<Wallet> wallets = walletRepository.findAll();

    Assertions.assertThat(wallets)
        .hasSize(4) // take in account the elements with which the app is started
        .extracting(Wallet::getBalance)
        .contains(BALANCE_OF_TEN, BALANCE_OF_TWENTY);
  }

  @Test
  @DisplayName("Save wallet")
  public void saveWallet_success() {
    Wallet wallet = new Wallet(UUID.randomUUID().toString(), BALANCE_OF_TEN);

    Wallet createdWallet = walletRepository.save(wallet);

    assertEquals(wallet.getId(), createdWallet.getId());
    assertEquals(wallet.getBalance(), createdWallet.getBalance());
    assertEquals(wallet.getCurrency(), createdWallet.getCurrency());
  }

  @Test
  @DisplayName("Update wallet")
  public void payWithWallet_update_success() {
    Wallet wallet = new Wallet(UUID.randomUUID().toString(), BALANCE_OF_TEN);
    Wallet savedWallet = walletRepository.save(wallet);

    assertEquals(wallet.getBalance(), savedWallet.getBalance());

    savedWallet.setBalance(BALANCE_OF_FIVE);

    Wallet updatedWallet = walletRepository.save(savedWallet);

    assertEquals(savedWallet.getId(), updatedWallet.getId());
    assertEquals(BALANCE_OF_FIVE, updatedWallet.getBalance());
  }
}
