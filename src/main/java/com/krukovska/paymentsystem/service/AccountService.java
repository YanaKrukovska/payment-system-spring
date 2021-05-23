package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface AccountService {
    Page<Account> findAllClientAccounts(Long clientId, PageRequest page);

    Account findAccountById(Long accountId);

    Response<Account> topUpAccount(Long accountId, double amount);

    Response<Account> blockAccount(Long accountId);

    void updateAccount(Account account);
}
