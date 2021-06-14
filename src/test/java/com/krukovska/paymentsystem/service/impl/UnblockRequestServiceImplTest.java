package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.*;
import com.krukovska.paymentsystem.persistence.repository.RequestRepository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnblockRequestServiceImplTest {

    @Mock
    private RequestRepository repo;

    @Mock
    private AccountService accountService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private UnblockRequestServiceImpl service;

    @Test
    void findRequestByIdSuccess() {
        service.findRequestById(1L);
        verify(repo, times(1)).findUnblockRequestById(anyLong());
    }

    @Test
    void findAllRequestsByClient() {
        service.findAllClientRequests(1L, Optional.of(1), Optional.of(3));
        verify(repo, times(1)).findAllByClientId(anyLong(), any(PageRequest.class));
    }

    @Test
    void createNewRequestSuccess() {
        when(accountService.findAccountById(anyLong())).thenReturn(new Account());
        when(clientService.findClientById(anyLong())).thenReturn(new Client());

        service.createNewRequest(1L, 2L);
        verify(repo, times(1)).save(any(UnblockRequest.class));
    }

    @Test
    void createNewRequestAccountNotExists() {
        when(accountService.findAccountById(anyLong())).thenReturn(null);
        Response<UnblockRequest> result = service.createNewRequest(1L, 2L);
        verify(repo, never()).save(any(UnblockRequest.class));
        assertThat(result.getErrors().get(0), equalTo("Account doesn't exist"));
    }

    @Test
    void createNewRequestClientNotExists() {
        when(accountService.findAccountById(anyLong())).thenReturn(new Account());
        when(clientService.findClientById(anyLong())).thenReturn(null);

        Response<UnblockRequest> result = service.createNewRequest(1L, 2L);
        verify(repo, never()).save(any(UnblockRequest.class));
        assertThat(result.getErrors().get(0), equalTo("Client doesn't exist"));
    }

    @Test
    void acceptUnblockRequestSuccess() {
        UnblockRequest unblockRequest = new UnblockRequest();
        Account account = new Account();
        account.setStatus(AccountStatus.BLOCKED);
        unblockRequest.setAccount(account);

        when(service.findRequestById(anyLong())).thenReturn(unblockRequest);

        service.updateRequest(1L, true);
        verify(accountService, times(1)).updateAccount(any(Account.class));
        verify(repo, times(1)).save(unblockRequest);
    }

    @Test
    void notAcceptUnblockRequestSuccess() {
        UnblockRequest unblockRequest = new UnblockRequest();
        Account account = new Account();
        account.setStatus(AccountStatus.BLOCKED);
        unblockRequest.setAccount(account);

        when(service.findRequestById(anyLong())).thenReturn(unblockRequest);

        service.updateRequest(1L, false);
        verify(accountService, never()).updateAccount(any(Account.class));
        verify(repo, times(1)).save(unblockRequest);
    }

    @Test
    void updateRequestNotExist() {
        when(service.findRequestById(anyLong())).thenReturn(null);

        Response<UnblockRequest> result = service.updateRequest(1L, true);
        verify(repo, never()).save(any(UnblockRequest.class));
        assertThat(result.getErrors().get(0), equalTo("Request doesn't exist"));
    }


}