package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.AccountStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.AccountRepository;
import com.krukovska.paymentsystem.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedList;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;

@Service

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public Page<Account> findAllClientAccounts(Long clientId, PageRequest page) {
        requireNonNull(clientId, "Client id must be not null");
        requireNonNull(page, "Client page must be not null");

        return accountRepository.findAllByClientId(clientId, page);
    }

    @Override
    public Account findAccountById(Long accountId) {
        requireNonNull(accountId, "Account id must be not null");
        return accountRepository.findAccountById(accountId);
    }

    @Override
    public Response<Account> topUpAccount(Long accountId, double amount) {
        requireNonNull(accountId, "Account id must be not null");

        if (amount <= 0) {
            return new Response<>(singletonList("You can't add less than 0"));
        }

        var account = findAccountById(accountId);

        if (account == null) {
            return new Response<>(singletonList("Account with id " + accountId + " does not exists"));
        }

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(amount)));

        return new Response<>(accountRepository.save(account));
    }

    @Override
    public Response<Account> blockAccount(Long accountId) {
        var account = findAccountById(accountId);

        if (account == null) {
            return new Response<>(singletonList("Account with id " + accountId + " does not exist"));
        }

        if (account.getStatus() == AccountStatus.BLOCKED) {
            return new Response<>(singletonList("Account  " + account.getIban() + " is already blocked"));
        } else {
            account.setStatus(AccountStatus.BLOCKED);
            return new Response<>(accountRepository.save(account), new LinkedList<>());
        }

    }

    @Override
    public void updateAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Response<Account> withdrawAmount(long accountId, BigDecimal amount) {
        var account = findAccountById(accountId);
        if (account == null) {
            return new Response<>(singletonList("Account with id " + accountId + " does not exists"));
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new Response<>(singletonList("Sum must be > 0. current amount =  " + amount));
        }

        if (account.getBalance().compareTo(amount) < 0) {
            return new Response<>(singletonList("Account balance: " + account.getBalance() + ". Required amount: " + amount));
        }

        account.setBalance(account.getBalance().subtract(amount));
        return new Response<>(accountRepository.save(account), new LinkedList<>());
    }
}
