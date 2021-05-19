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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.krukovska.paymentsystem.util.Constants.*;

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

        model.addAttribute("sortDir", sortDir.orElse(DEFAULT_SORTING_ORDER));
        model.addAttribute("reverseSortDir", sortDir.map(s -> s.equals("asc") ? "desc" : "asc")
                .orElse(DEFAULT_REVERSE_SORTING_ORDER));
        model.addAttribute("sortField", sortField.orElse(DEFAULT_SORTING_FIELD));
        model.addAttribute("page", page.orElse(DEFAULT_CURRENT_PAGE));

        Page<Payment> payPage = paymentService.findAllClientPayments(CLIENT_ID, page, size, sortField, sortDir);
        model.addAttribute("payPage", payPage);

        int totalPages = payPage.getTotalPages();
        if (totalPages > 0) {
            model.addAttribute("pageNumbers", IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList()));
        }

        return "payments";
    }
}
