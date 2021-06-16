package com.krukovska.paymentsystem.persistence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "credit_cards")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "card_number", name = "CARD_NUMBER_UNIQUE_CONSTRAINT"))
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_ACCOUNT_ID"))
    private Account account;

    @Column(nullable = false, name = "card_number")
    private String cardNumber;

    @Column(nullable = false)
    private boolean isExpired;

    @Override
    public String toString() {
        return "CreditCard: { account = " + account.getId() + ", cardNumber = '" + cardNumber +
                ", isExpired = " + isExpired + '}';
    }
}
