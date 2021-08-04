package com.playtomic.tests.wallet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "wallet")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {

  private static final String EUR = "EUR";

  @Id
  @Column(nullable = false)
  private String id;

  private BigDecimal balance;

  private String currency = EUR;

  @OneToMany(targetEntity = WalletTransactions.class)
  @ToString.Exclude // @ToString includes lazy loaded fields and/or associations. This can cause
  // performance and memory consumption issues.
  private Set<WalletTransactions> walletTransactionsSet;

  public Wallet(String id, BigDecimal amount) {
    this.id = id;
    this.balance = amount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Wallet wallet = (Wallet) o;

    return Objects.equals(id, wallet.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, balance, currency, walletTransactionsSet);
  }
}
