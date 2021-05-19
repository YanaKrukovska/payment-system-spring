package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.*;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Page<Payment> findAllClientPayments(Long clientId, Optional<Integer> page, Optional<Integer> size,
                                               Optional<String> sortField, Optional<String> sortDirection) {

        Sort sort = sortDirection.orElse(DEFAULT_SORTING_ORDER).equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField.orElse(DEFAULT_SORTING_FIELD)).ascending() :
                Sort.by(sortField.orElse(DEFAULT_SORTING_FIELD)).descending();

        return paymentRepository.findAllByAccountClientId(clientId,
                PageRequest.of(page.orElse(DEFAULT_CURRENT_PAGE) - 1, size.orElse(DEFAULT_PAGE_SIZE), sort));
    }

}
