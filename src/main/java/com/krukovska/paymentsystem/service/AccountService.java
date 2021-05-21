package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.AccountRepository;
import com.krukovska.paymentsystem.util.SortHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.DEFAULT_CURRENT_PAGE;
import static com.krukovska.paymentsystem.util.Constants.DEFAULT_PAGE_SIZE;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Page<Account> findAllClientAccounts(Long clientId, Optional<Integer> page, Optional<Integer> size,
                                               Optional<String> sortField, Optional<String> sortDirection) {
        return accountRepository.findAllByClientId(clientId,
                PageRequest.of(
                        page.orElse(DEFAULT_CURRENT_PAGE) - 1,
                        size.orElse(DEFAULT_PAGE_SIZE),
                        SortHelper.buildSort(sortField, sortDirection)));
    }

    public Account findAccountById(String accountId) {
        return accountRepository.findById(Long.valueOf(accountId)).orElse(null);
    }

    public Response<Account> topUpAccount(String accountId, double amount) {
        Account account = findAccountById(accountId);

        if (account == null) {
            return new Response<>(null, Collections.singletonList("Account doesn't exist"));
        }

        if (amount <= 0) {
            return new Response<>(account, Collections.singletonList("You can't add less than 0"));
        }

        account.setBalance(account.getBalance().add(BigDecimal.valueOf(amount)));

        return new Response<>(accountRepository.save(account), new LinkedList<>());
    }
}
