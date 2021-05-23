package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.DEFAULT_CURRENT_PAGE;
import static com.krukovska.paymentsystem.util.Constants.DEFAULT_PAGE_SIZE;
import static com.krukovska.paymentsystem.util.SortHelper.buildSort;

@Service
//TODO add interface
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Page<Payment> findAllClientPayments(Long clientId, Optional<Integer> page, Optional<Integer> size,
                                               Optional<String> sortField, Optional<String> sortDirection) {
        return paymentRepository.findAllByAccountClientId(clientId,
                PageRequest.of(page.orElse(DEFAULT_CURRENT_PAGE) - 1, size.orElse(DEFAULT_PAGE_SIZE),
                        buildSort(sortField, sortDirection)));
    }

}
