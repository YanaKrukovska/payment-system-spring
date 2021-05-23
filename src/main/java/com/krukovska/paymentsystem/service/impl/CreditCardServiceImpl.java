package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.CreditCard;
import com.krukovska.paymentsystem.persistence.repository.CreditCardRepository;
import com.krukovska.paymentsystem.service.CreditCardService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    @Override
    public CreditCard findCardByCardNumber(String cardNumber){
        return creditCardRepository.findByCardNumber(cardNumber);
    }

    @Override
    public List<CreditCard> findAllClientCreditCards(Long clientId) {
        return creditCardRepository.findAllByAccountClientId(clientId);
    }
}
