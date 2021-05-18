package com.krukovska.paymentsystem.persistence.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ClientStatus {

    BLOCKED("Blocked"),
    ACTIVE("Active");

    private final String name;

}
