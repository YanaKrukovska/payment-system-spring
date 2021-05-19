package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.service.CreditCardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.krukovska.paymentsystem.util.Constants.CLIENT_ID;

@Controller
@RequestMapping("/card")
public class CardController {

    private final CreditCardService creditCardService;

    public CardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping("/all")
    public String getAllClientCreditCards( Model model) {
        model.addAttribute("cards", creditCardService.findAllClientCreditCards(CLIENT_ID));
        return "cards";
    }

    @GetMapping("/{cardNumber}")
    public String getCardByCardNumber(@PathVariable String cardNumber, Model model) {
        model.addAttribute("card", creditCardService.findCardByCardNumber(cardNumber));
        return "card";
    }
}
