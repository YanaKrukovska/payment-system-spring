package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Account;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;
    private final ClientService clientService;
    public static final long CLIENT_ID = 2L;

    public AccountController(AccountService accountService, ClientService clientService) {
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @GetMapping(value = "/all")
    public String allAccounts(Model model, @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        Page<Account> bookPage = accountService.findAccountsByClient(
                clientService.findClientById(CLIENT_ID),
                PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("accountPage", bookPage);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "accounts";
    }
}
