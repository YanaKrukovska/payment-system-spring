package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.repository.AccountRepository;
import com.krukovska.paymentsystem.util.SortHelper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

}
