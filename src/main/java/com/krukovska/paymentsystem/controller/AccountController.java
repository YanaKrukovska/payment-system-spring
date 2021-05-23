package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.service.impl.AccountServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.CLIENT_ID;
import static com.krukovska.paymentsystem.util.ModelHelper.setSortingPaginationAttributes;
import static com.krukovska.paymentsystem.util.PageRequestHelper.createPageRequest;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountServiceImpl accountService;

    public AccountController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/all")
    public String allAccounts(Model model, @RequestParam("page") Optional<Integer> pageNumber,
                              @RequestParam("size") Optional<Integer> pageSize,
                              @RequestParam("sortField") Optional<String> sortField,
                              @RequestParam("sortDir") Optional<String> sortDir) {

        Page<Account> accPage = accountService.findAllClientAccounts(CLIENT_ID, createPageRequest(pageNumber, pageSize, sortField, sortDir));
        model.addAttribute("accountPage", accPage);
        setSortingPaginationAttributes(model, pageNumber, sortField, sortDir, accPage);
        return "accounts";
    }

    @GetMapping("/topup/{accountId}")
    public String getTopUpPage(Model model, @PathVariable Long accountId) {

        if (accountId == null) {
            //TODO add localization
            model.addAttribute("message", "Account ID must be not empty");
            return "error";
        }

        var account = accountService.findAccountById(accountId);
        if (account == null) {
            //TODO add localization
            model.addAttribute("message", "Account doesn't exist");
            return "error";
        }

        model.addAttribute("accountId", accountId);
        return "account-topup";
    }

    @PostMapping("/topup/{accountId}")
    public String topUpAccount(Model model, @PathVariable Long accountId, @ModelAttribute("amount") double amount) {

        List<String> errors = new ArrayList<>();
        if (accountId == null) {
            //TODO add localization
            errors.add("Account ID must be not empty");
        }

        if (amount <= 0) {
            //TODO add localization
            errors.add("Amount must be greater than 0 ");
        }

        if (errors.isEmpty()) {
            Response<Account> updateResponse = accountService.topUpAccount(accountId, amount);
            if (updateResponse.hasErrors()) {
                errors.addAll(updateResponse.getErrors());
            }
        }

        if (errors.isEmpty()) {
            return "redirect:/account/all";
        } else {
            model.addAttribute("errors", errors);
            return "account-topup";
        }

    }

    @PostMapping("/block/{accountId}")
    public String blockAccount(Model model, @PathVariable Long accountId) {
        List<String> errors = new ArrayList<>();
        if (accountId == null) {
            //TODO add localization
            errors.add("Account ID must be not empty");
        }
        if (errors.isEmpty()) {
            Response<Account> updateResponse = accountService.blockAccount(accountId);
            if (updateResponse.hasErrors()) {
                errors.addAll(updateResponse.getErrors());
            }
        }

        model.addAttribute("errors", errors);

        return "redirect:/account/all";
    }


}
