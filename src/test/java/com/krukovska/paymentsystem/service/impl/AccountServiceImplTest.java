package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.*;
import com.krukovska.paymentsystem.persistence.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository repo;

    @InjectMocks
    private AccountServiceImpl service;

    private final PageRequest defaultPage = PageRequest.of(1, 5, Sort.by(Sort.Order.asc("id")));


    @Test
    void findByIdSuccess() {
        service.findAccountById(1L);
        verify(repo, times(1)).findAccountById(1L);
    }

    @Test
    void findAllClientAccountsSuccess() {
        service.findAllClientAccounts(1L, defaultPage);
        verify(repo, times(1)).findAllByClientId(1L, defaultPage);
    }

    @Test
    void findAllClientAccountsByNullClientId() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                service.findAllClientAccounts(null, defaultPage)
        );
        assertThat(exception.getMessage(), equalTo("Client id must be not null"));
    }

    @Test
    void findAllClientAccountsByNullPage() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                service.findAllClientAccounts(1L, null)
        );
        assertThat(exception.getMessage(), equalTo("Client page must be not null"));
    }

    @Test
    void findAccountByIdSuccess() {
        service.findAccountById(1L);
        verify(repo, times(1)).findAccountById(anyLong());
    }

    @Test
    void findAccountByNullId() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                service.findAccountById(null)
        );
        assertThat(exception.getMessage(), equalTo("Account id must be not null"));
    }

    @Test
    void updateAccount() {
        Account a = new Account();
        service.updateAccount(a);
        verify(repo, times(1)).save(a);
    }

    @Test
    void blockAccountSuccess() {
        Account account = new Account();
        account.setStatus(AccountStatus.ACTIVE);
        when(repo.findAccountById(anyLong())).thenReturn(account);

        service.blockAccount(1L);

        verify(repo, times(1)).findAccountById(anyLong());
        verify(repo, times(1)).save(account);
        assertThat(account.getStatus(), equalTo(AccountStatus.BLOCKED));
    }

    @Test
    void blockAccountNotExists() {
        when(repo.findAccountById(anyLong())).thenReturn(null);
        Response<Account> result = service.blockAccount(1L);

        verify(repo, times(1)).findAccountById(anyLong());
        verify(repo, never()).save(any());
        assertThat(result.getErrors().get(0), equalTo("Account with id " + 1L + " does not exist"));
    }

    @Test
    void blockAccountAlreadyBlocked() {
        Account account = new Account();
        account.setIban("UA00000_4");
        account.setStatus(AccountStatus.BLOCKED);
        when(repo.findAccountById(anyLong())).thenReturn(account);

        Response<Account> result = service.blockAccount(1L);

        verify(repo, times(1)).findAccountById(anyLong());
        verify(repo, never()).save(any());
        assertThat(result.getErrors().get(0), equalTo("Account  " + account.getIban() + " is already blocked"));
    }

    @Test
    void topUpAccountNotExists() {
        when(repo.findAccountById(anyLong())).thenReturn(null);
        Response<Account> result = service.topUpAccount(1L, 100);

        verify(repo, times(1)).findAccountById(anyLong());
        verify(repo, never()).save(any());
        assertThat(result.getErrors().get(0), equalTo("Account with id " + 1L + " does not exists"));
    }

    @Test
    void topUpAccountNullId() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                service.topUpAccount(null, 100)
        );
        assertThat(exception.getMessage(), equalTo("Account id must be not null"));
    }

    @Test
    void topUpAccountNegativeAmount() {
        Response<Account> result = service.topUpAccount(1L, -100);
        assertThat(result.getErrors().get(0), equalTo("You can't add less than 0"));
        verify(repo, never()).save(any());
    }

    @Test
    void topUpAccountSuccess() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(100L));
        when(repo.findAccountById(anyLong())).thenReturn(account);

        service.topUpAccount(1L, 200L);

        verify(repo, times(1)).findAccountById(anyLong());
        verify(repo, times(1)).save(account);
        assertEquals(0, account.getBalance().compareTo(BigDecimal.valueOf(300)));
    }

    @Test
    void withdrawAccountNotExists() {
        when(repo.findAccountById(anyLong())).thenReturn(null);
        Response<Account> result = service.withdrawAmount(1L, BigDecimal.valueOf(100));

        verify(repo, times(1)).findAccountById(anyLong());
        verify(repo, never()).save(any());
        assertThat(result.getErrors().get(0), equalTo("Account with id " + 1L + " does not exists"));
    }

    @Test
    void withdrawAccountSuccess() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(200));
        when(repo.findAccountById(anyLong())).thenReturn(account);

        service.withdrawAmount(1L, BigDecimal.valueOf(100));

        verify(repo, times(1)).findAccountById(anyLong());
        verify(repo, times(1)).save(account);
        assertEquals(0, account.getBalance().compareTo(BigDecimal.valueOf(100)));
    }

    @Test
    void withdrawAccountNotEnoughBalance() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(200));
        when(repo.findAccountById(anyLong())).thenReturn(account);

        int amountToWithdraw = 500;
        Response<Account> result = service.withdrawAmount(1L, BigDecimal.valueOf(amountToWithdraw));

        verify(repo, times(1)).findAccountById(anyLong());
        verify(repo, never()).save(any());
        assertThat(result.getErrors().get(0), equalTo("Account balance: " +
                account.getBalance() + ". Required amount: " + amountToWithdraw));
    }

    @Test
    void withdrawAccountNegativeAmount() {
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(200));
        when(repo.findAccountById(anyLong())).thenReturn(account);

        int amountToWithdraw = -100;
        Response<Account> result = service.withdrawAmount(1L, BigDecimal.valueOf(amountToWithdraw));

        verify(repo, times(1)).findAccountById(anyLong());
        verify(repo, never()).save(any());
        assertThat(result.getErrors().get(0), equalTo("Sum must be > 0. current amount =  " + amountToWithdraw));
    }
}