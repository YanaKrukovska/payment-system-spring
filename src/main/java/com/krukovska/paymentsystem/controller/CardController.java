package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.service.impl.CreditCardServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.krukovska.paymentsystem.util.Constants.CLIENT_ID;

@Controller
@RequestMapping("/card")
public class CardController {

    private final CreditCardServiceImpl creditCardService;

    public CardController(CreditCardServiceImpl creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping("/all")
    public String getAllClientCreditCards(Model model) {
        model.addAttribute("cards", creditCardService.findAllClientCreditCards(CLIENT_ID));
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
