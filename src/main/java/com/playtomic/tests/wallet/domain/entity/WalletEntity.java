package com.playtomic.tests.wallet.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallet")
public class WalletEntity {

    @Id
    @Column(name = "wallet_id")
    private String id;

    @Column(name = "description")
    private String description;
}
