package com.krukovska.paymentsystem.persistence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "accounts", indexes = {@Index(columnList = "name", name = "ACCOUNT_NAME_INDEX"),
        @Index(columnList = "balance", name = "PAYMENT_BALANCE_INDEX")})
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, foreignKey = @ForeignKey(name = "FK_CLIENT"))
    private Client client;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Column(nullable = false)
    private String iban;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_CREDIT_CARD"))
    private CreditCard creditCard;

}
