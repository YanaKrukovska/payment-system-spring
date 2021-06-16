package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PaymentService {

    Page<Payment> findAllClientPayments(Long clientId, PageRequest pageRequest);

    Payment create(Payment payment);

    Response<Payment> send(long paymentId);
}
