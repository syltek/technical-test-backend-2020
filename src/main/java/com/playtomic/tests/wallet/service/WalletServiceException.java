package com.playtomic.tests.wallet.service;

public class WalletServiceException extends RuntimeException {

  private int code;

  WalletServiceException(int code, String message) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
