package com.krukovska.paymentsystem.persistence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "unblock_requests")
public class UnblockRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false, foreignKey = @ForeignKey(name = "FK_UNBLOCK_REQUEST_CLIENT"))
    private Client client;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, foreignKey = @ForeignKey(name = "FK_UNBLOCK_REQUEST_ACCOUNT"))
    private Account account;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column()
    private LocalDate actionDate;

    @Override
    public String toString() {
        return "UnblockRequest{" +
                "id=" + id +
                ", client=" + client +
                ", account=" + account +
                ", creationDate=" + creationDate +
                ", actionDate=" + actionDate +
                '}';
    }
}
