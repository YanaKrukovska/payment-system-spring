package com.krukovska.paymentsystem.persistence.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email", name = "EMAIL_UNIQUE_CONSTRAINT"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean isAdmin;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_USER_CLIENT_ID"))
    private Client client;

    @Override
    public String toString() {
        return "User: {email = '" + email + ", name = '" + name +
                ", password = '" + password + ", isAdmin = " + isAdmin +
                ", client = " + client.getId() + '}';
    }
}
