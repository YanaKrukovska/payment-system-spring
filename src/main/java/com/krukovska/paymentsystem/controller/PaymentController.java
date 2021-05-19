package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.krukovska.paymentsystem.util.Constants.CLIENT_ID;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/all")
    public String getAllClientPayments(Model model, @RequestParam("page") Optional<Integer> page,
                                       @RequestParam("size") Optional<Integer> size) {

        Page<Payment> payPage = paymentService.findAllClientPayments(CLIENT_ID, page, size);
        model.addAttribute("payPage", payPage);

        int totalPages = payPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "payments";
    }
}
