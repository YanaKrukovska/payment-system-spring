package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    List<CreditCard> findAllByAccountClientId(Long clientId);

    CreditCard findByCardNumber(String cardNumber);
}
