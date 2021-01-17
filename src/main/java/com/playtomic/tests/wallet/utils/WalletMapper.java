package com.playtomic.tests.wallet.utils;

import com.playtomic.tests.wallet.data.Wallet;
import com.playtomic.tests.wallet.data.dto.WalletDTO;

public class WalletMapper {
    private WalletMapper(){}

    public static WalletDTO walletToWalletDTOMapper(Wallet  wallet){
        WalletDTO  walletDTO = new WalletDTO();
        walletDTO.setId(wallet.getId());
        walletDTO.setSecondName(wallet.getSecondName());
        walletDTO.setFirstName(wallet.getFirstName());
        walletDTO.setBalance(wallet.getBalance());

        return walletDTO;
    }
}
