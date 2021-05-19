package com.krukovska.paymentsystem.service;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final ClientService clientService;

    public AccountService(ClientService clientService) {
        this.clientService = clientService;
    }

}
