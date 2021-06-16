package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.User;
import com.krukovska.paymentsystem.service.impl.CreditCardServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/card")
public class CardController {

    private final CreditCardServiceImpl creditCardService;

    public CardController(CreditCardServiceImpl creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping("/all")
    public String getAllClientCreditCards(Model model) {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("cards", creditCardService.findAllClientCreditCards(user.getClient().getId()));
        return "cards";
    }

    @GetMapping("/{cardNumber}")
    public String getCardByCardNumber(@PathVariable String cardNumber, Model model) {

        //TODO add localization
        if (cardNumber == null || cardNumber.equals("")) {
            model.addAttribute("message", "Card number can't be empty");
            return "error";
        }

        var creditCard = creditCardService.findCardByCardNumber(cardNumber);
        if (creditCard == null) {
            model.addAttribute("message", "Card doesn't exist");
            return "error";
        }

        model.addAttribute("card", creditCard);
        return "card";
    }
}
