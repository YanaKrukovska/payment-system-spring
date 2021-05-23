package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.CreditCard;
import com.krukovska.paymentsystem.persistence.repository.CreditCardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
//TODO add interface
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;

    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    public CreditCard findCardByCardNumber(String cardNumber){
        return creditCardRepository.findByCardNumber(cardNumber);
    }

    public List<CreditCard> findAllClientCreditCards(Long clientId) {
        return creditCardRepository.findAllByAccountClientId(clientId);
    }
}
