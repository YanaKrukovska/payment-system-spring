package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.CLIENT_ID;
import static com.krukovska.paymentsystem.util.ModelHelper.setPaginationAttributes;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(value = "/all")
    public String allAccounts(Model model, @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size,
                              @RequestParam("sortField") Optional<String> sortField,
                              @RequestParam("sortDir") Optional<String> sortDir) {

        Page<Account> accPage = accountService.findAllClientAccounts(CLIENT_ID, page, size, sortField, sortDir);
        model.addAttribute("accountPage", accPage);
        setPaginationAttributes(model, page, sortField, sortDir, accPage);
        return "accounts";
    }

    @GetMapping("/topup")
    public String getTopUpPage(Model model, @RequestParam String accountId) {
        model.addAttribute("accountId", accountId);
        return "account-topup";
    }

    @PostMapping("/topup")
    public String topUpAccount(Model model, @RequestParam String accountId, @ModelAttribute("amount") double amount) {
        Response<Account> errors = accountService.topUpAccount(accountId, amount);

        if (!errors.isOkay()){
            model.addAttribute("errors", errors.getErrors());
            return "account-topup";
        }
        return "redirect:/account/all";
    }
}
