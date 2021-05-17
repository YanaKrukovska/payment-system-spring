package com.krukovska.paymentsystem.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AccountStatus {

    BLOCKED("Blocked"),
    ACTIVE("Active");

    private final String name;

}
