package com.krukovska.paymentsystem.persistence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
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
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column()
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
}
