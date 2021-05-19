package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static com.krukovska.paymentsystem.controller.CardController.CLIENT_ID;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final ClientService clientService;

    public AccountController(AccountService accountService, ClientService clientService) {
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @GetMapping(value = "/all")
    public String allAccounts(Model model) {
        List<Account> accounts = clientService.findClientById(CLIENT_ID).getAccounts();
        model.addAttribute("accounts", accounts);
        return "accounts";
    }
}
