package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.PaymentStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.model.User;
import com.krukovska.paymentsystem.service.PaymentProcessingException;
import com.krukovska.paymentsystem.service.impl.AccountServiceImpl;
import com.krukovska.paymentsystem.service.impl.PaymentServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.ERROR_LABEL;
import static com.krukovska.paymentsystem.util.ModelHelper.setSortingPaginationAttributes;
import static com.krukovska.paymentsystem.util.PageRequestHelper.createPageRequest;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentServiceImpl paymentService;
    private final AccountServiceImpl accountService;

    public PaymentController(PaymentServiceImpl paymentService, AccountServiceImpl accountService) {
        this.paymentService = paymentService;
        this.accountService = accountService;
    }

    @GetMapping("/all")
    public String getAllClientPayments(Model model, @RequestParam("page") Optional<Integer> page,
                                       @RequestParam("size") Optional<Integer> size,
                                       @RequestParam("sortField") Optional<String> sortField,
                                       @RequestParam("sortDir") Optional<String> sortDir) {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Page<Payment> payPage = paymentService.findAllClientPayments(user.getClient().getId(),
                createPageRequest(page, size, sortField, sortDir));
        model.addAttribute("payPage", payPage);
        setSortingPaginationAttributes(model, page, sortField, sortDir, payPage);
        return "payments";
    }

    @GetMapping("/add")
    public String getPaymentCreationPage(Model model, @RequestParam Long accountId) {

        var account = accountService.findAccountById(accountId);
        if (account == null) {
            model.addAttribute("errors", Collections.singletonList("Accoutn doesn't exist"));
            return "payments";
        }

        var payment = new Payment();
        payment.setAccount(account);
        model.addAttribute("payment", payment);
        model.addAttribute("accountId", accountId);
        model.addAttribute("balance", account.getBalance());

        return "payment-add";
    }


    @PostMapping("/add")
    public String createNewPayment(@ModelAttribute Payment payment) {

        payment.setStatus(PaymentStatus.CREATED);
        payment.setPaymentDate(LocalDate.now());

        paymentService.create(payment);

        return "redirect:/payment/all";
    }

    @PostMapping("/send/{paymentId}")
    public String sendPayment(@PathVariable Long paymentId, Model model) {
        try {
            Response<Payment> sendResponse = paymentService.send(paymentId);

            if (sendResponse.hasErrors()) {
                model.addAttribute("message", sendResponse.getErrors());
                return ERROR_LABEL;
            }

        } catch (PaymentProcessingException e) {
            model.addAttribute("message", e.getMessage());
            return ERROR_LABEL;
        }
        return "redirect:/payment/all";
    }
}
