package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AccountService {

    private final ClientService clientService;

    public AccountService(ClientService clientService) {
        this.clientService = clientService;
    }

    public Page<Account> findAccountsByClient(Client client, Pageable pageable) {
        List<Account> accounts = clientService.findClientById(client.getId()).getAccounts();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Account> list;

        if (accounts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, accounts.size());
            list = accounts.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), accounts.size());
    }

}
