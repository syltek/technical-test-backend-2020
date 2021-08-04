package com.playtomic.tests.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

public class WalletTransactionDTO {
  @Data
  @NoArgsConstructor(access = PROTECTED)
  @AllArgsConstructor()
  public static class WalletTransactionReq {
    Long id;
    String walletId;
    BigDecimal amount;
    Long updatedAt;
  }

  @Data
  @NoArgsConstructor(access = PROTECTED)
  @AllArgsConstructor
  @Builder
  public static class WalletTransactionRes {
    Long id;
    String walletId;
    BigDecimal amount;
    Long updatedAt;
  }
}
