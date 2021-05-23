package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.CreditCard;

import java.util.List;

public interface CreditCardService {

    CreditCard findCardByCardNumber(String cardNumber);

    List<CreditCard> findAllClientCreditCards(Long clientId);
}
