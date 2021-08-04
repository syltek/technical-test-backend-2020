package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exceptions.PaymentServiceException;

import java.math.BigDecimal;

public interface PaymentService {
  void charge(BigDecimal bigDecimal) throws PaymentServiceException;
}
