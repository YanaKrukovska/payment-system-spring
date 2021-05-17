package com.krukovska.paymentsystem.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "FK_USER_ID"))
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientStatus status;

    @OneToMany(mappedBy = "client")
    private List<Account> accounts;

}
