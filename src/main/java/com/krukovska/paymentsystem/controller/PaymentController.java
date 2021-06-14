package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.PaymentStatus;
import com.krukovska.paymentsystem.service.impl.AccountServiceImpl;
import com.krukovska.paymentsystem.service.impl.PaymentServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.CLIENT_ID;
import static com.krukovska.paymentsystem.util.ModelHelper.setSortingPaginationAttributes;
import static com.krukovska.paymentsystem.util.PageRequestHelper.createPageRequest;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final Logger log = LogManager.getLogger(this.getClass());


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
        Page<Payment> payPage = paymentService.findAllClientPayments(CLIENT_ID,createPageRequest(page, size, sortField, sortDir));
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
    public String sendPayment(@PathVariable Long paymentId) {
        paymentService.send(paymentId);
        return "redirect:/payment/all";
    }
}
