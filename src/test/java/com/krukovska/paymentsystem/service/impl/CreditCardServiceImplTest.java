package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.CreditCard;
import com.krukovska.paymentsystem.persistence.repository.CreditCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceImplTest {

    @InjectMocks
    private CreditCardServiceImpl service;

    @Mock
    private CreditCardRepository repo;

    @Test
    void findByCardNumberSuccess() {
        when(repo.findByCardNumber("0000111122223333")).thenReturn(new CreditCard());
        assertNotNull(service.findCardByCardNumber("0000111122223333"));
        verify(repo, times(1)).findByCardNumber("0000111122223333");
    }

    @Test
    void findAllCreditCardsByClientAccount() {
        when(repo.findAllByAccountClientId(anyLong())).thenReturn(Collections.singletonList(new CreditCard()));
        service.findAllClientCreditCards(1L);
        verify(repo, times(1)).findAllByAccountClientId(anyLong());
    }

}