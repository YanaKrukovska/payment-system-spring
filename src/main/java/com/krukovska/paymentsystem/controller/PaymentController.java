package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.CLIENT_ID;
import static com.krukovska.paymentsystem.util.ModelHelper.setPaginationAttributes;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/all")
    public String getAllClientPayments(Model model, @RequestParam("page") Optional<Integer> page,
                                       @RequestParam("size") Optional<Integer> size,
                                       @RequestParam("sortField") Optional<String> sortField,
                                       @RequestParam("sortDir") Optional<String> sortDir) {
        Page<Payment> payPage = paymentService.findAllClientPayments(CLIENT_ID, page, size, sortField, sortDir);
        model.addAttribute("payPage", payPage);
        setPaginationAttributes(model, page, sortField, sortDir, payPage);
        return "payments";
    }
}
