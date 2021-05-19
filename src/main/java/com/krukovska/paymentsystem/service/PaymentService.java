package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.DEFAULT_CURRENT_PAGE;
import static com.krukovska.paymentsystem.util.Constants.DEFAULT_PAGE_SIZE;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Page<Payment> findAllClientPayments(Long clientId, Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(DEFAULT_CURRENT_PAGE);
        int pageSize = size.orElse(DEFAULT_PAGE_SIZE);
        return paymentRepository.findAllByAccountClientId(clientId, PageRequest.of(currentPage - 1, pageSize));
    }

}
