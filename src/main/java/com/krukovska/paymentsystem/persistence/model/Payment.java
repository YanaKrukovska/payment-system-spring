package com.krukovska.paymentsystem.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "payments")
@Table(indexes = {@Index(columnList = "payment_date", name = "PAYMENT_DATE_INDEX"),
        @Index(columnList = "account_id", name = "PAYMENT_ACCOUNT_INDEX")})
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PAYMENT_ACCOUNT"))
    private Account account;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private String receiverIban;

    @Column()
    private String details;

    @Column(nullable = false, name = "payment_date")
    private LocalDate paymentDate;

    @Override
    public String toString() {
        return "Payment: {account = " + account.getId() + ", amount = " + amount + ", status = " + status +
                ", receiverIban = '" + receiverIban + ", details = '" + details +
                ", paymentDate = " + paymentDate + '}';
    }
}
