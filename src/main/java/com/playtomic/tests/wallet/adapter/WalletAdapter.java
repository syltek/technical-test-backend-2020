package com.playtomic.tests.wallet.adapter;

import com.playtomic.tests.wallet.Currency;
import com.playtomic.tests.wallet.dto.WalletDTO;
import com.playtomic.tests.wallet.model.Wallet;

public class WalletAdapter {
  public static Wallet ToWallet(WalletDTO.WalletReq walletReq) {
    return Wallet.builder()
        .id(walletReq.getId())
        .balance(walletReq.getBalance())
        .currency(walletReq.getCurrency().toString())
        .build();
  }

  public static WalletDTO.WalletRes ToWalletRes(Wallet wallet) {
    return WalletDTO.WalletRes.builder()
        .id(wallet.getId())
        .balance(wallet.getBalance())
        .currency(Currency.valueOf(wallet.getCurrency()))
        .build();
  }

  public static Wallet ToWallet(WalletDTO.WalletRes walletRes) {
    return Wallet.builder()
        .id(walletRes.getId())
        .balance(walletRes.getBalance())
        .currency(walletRes.getCurrency().toString())
        .build();
  }
}
