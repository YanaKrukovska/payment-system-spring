package com.krukovska.paymentsystem.persistence.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PaymentStatus {

    CREATED("Created"),
    SENT("Sent");

    private final String name;

}
