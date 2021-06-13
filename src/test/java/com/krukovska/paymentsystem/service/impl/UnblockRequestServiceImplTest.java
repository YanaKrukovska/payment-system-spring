package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.CreditCard;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;
import com.krukovska.paymentsystem.persistence.repository.RequestRepository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.ClientService;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

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
        verify(repo, times(1)).findById(anyLong());
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


}