package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Payment;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PaymentService {

    Page<Payment> findAllClientPayments(Long clientId, Optional<Integer> page, Optional<Integer> size,
                                        Optional<String> sortField, Optional<String> sortDirection);

    Payment create(Payment payment);
}
