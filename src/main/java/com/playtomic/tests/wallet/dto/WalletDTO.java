package com.playtomic.tests.wallet.dto;

import com.playtomic.tests.wallet.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PROTECTED;

public class WalletDTO {

  @Data
  @NoArgsConstructor(access = PROTECTED)
  @AllArgsConstructor()
  public static class WalletReq {
    String id;
    BigDecimal balance;
    Currency currency;
  }

  @Data
  @NoArgsConstructor(access = PROTECTED)
  @AllArgsConstructor
  @Builder
  public static class WalletRes {
    String id;
    BigDecimal balance;
    Currency currency;
  }
}
