package com.krukovska.paymentsystem.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentStatus {

    CREATED("Created"),
    SENT("Sent");

    private final String name;

}
