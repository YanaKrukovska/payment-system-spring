package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.PaymentStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepository;
import com.krukovska.paymentsystem.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;

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
    void sendPaymentSuccess() {
        Payment payment = new Payment();
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.CREATED);
        Account account = new Account();
        account.setId(1L);
        payment.setAccount(account);

        when(repo.findById(anyLong())).thenReturn(payment);
        service.send(1L);

        verify(repo, times(1)).findById(anyLong());
        verify(accountService, times(1)).withdrawAmount(1L, payment.getAmount());
    }

    @Test
    void sendPaymentNotExist() {
        when(repo.findById(anyLong())).thenReturn(null);
        Response<Payment> result = service.send(1L);

        verify(repo, times(1)).findById(anyLong());
        verify(repo, never()).save(any());
        assertThat(result.getErrors().get(0), equalTo("Payment doesn't exist"));
    }


}