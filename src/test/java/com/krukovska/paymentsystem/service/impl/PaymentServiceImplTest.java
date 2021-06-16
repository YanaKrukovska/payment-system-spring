package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.*;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.ClientService;
import com.krukovska.paymentsystem.service.PaymentProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository repo;

    @Mock
    private AccountService accountService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private PaymentServiceImpl service;

    private final PageRequest defaultPage = PageRequest.of(1, 5, Sort.by(Sort.Order.asc("id")));

    @Test
    void findAllClientPaymentsSuccess() {
        service.findAllClientPayments(1L, defaultPage);
        verify(repo, times(1)).findAllByAccountClientId(1L, defaultPage);
    }

    @Test
    void findAllClientPaymentsByNullClientId() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                service.findAllClientPayments(null, defaultPage)
        );
        assertThat(exception.getMessage(), equalTo("Client id must be not null"));
    }

    @Test
    void findAllClientPaymentsByNullPage() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                service.findAllClientPayments(1L, null)
        );
        assertThat(exception.getMessage(), equalTo("Client page must be not null"));
    }

    @Test
    void createPaymentSuccess() {
        service.create(new Payment());
        verify(repo, times(1)).save(any(Payment.class));
    }

    @Test
    void createNullPayment() {
        Exception exception = assertThrows(NullPointerException.class, () ->
                service.create(null)
        );
        assertThat(exception.getMessage(), equalTo("Payment must be not null"));
    }

    @Test
    void sendPaymentNotExist() {
        when(repo.findById(anyLong())).thenReturn(null);

        Exception exception = assertThrows(PaymentProcessingException.class, () ->
                service.send(1L)
        );

        verify(repo, times(1)).findById(anyLong());
        verify(repo, never()).save(any());
        assertThat(exception.getMessage(), equalTo("Payment with id 1 does not exist"));
    }

    @Test
    void sendPaymentAlreadySent() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setStatus(PaymentStatus.SENT);
        when(repo.findById(anyLong())).thenReturn(payment);

        Exception exception = assertThrows(PaymentProcessingException.class, () ->
                service.send(1L)
        );

        verify(repo, times(1)).findById(anyLong());
        verify(repo, never()).save(any());
        assertThat(exception.getMessage(), equalTo("Payment with id " + payment.getId() + " is already sent"));
    }

    @Test
    void sendPaymentBlockedUser() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.CREATED);
        Client client = new Client();
        client.setId(1L);
        client.setStatus(ClientStatus.BLOCKED);
        Account account = new Account();
        account.setId(1L);
        account.setClient(client);
        payment.setAccount(account);

        when(repo.findById(anyLong())).thenReturn(payment);
        when(accountService.findAccountById(anyLong())).thenReturn(account);
        when(clientService.findClientById(anyLong())).thenReturn(client);

        Exception exception = assertThrows(PaymentProcessingException.class, () ->
                service.send(1L)
        );

        verify(repo, never()).save(any());
        assertThat(exception.getMessage(), equalTo("Client with id " + client.getId() + " is blocked"));
    }

}