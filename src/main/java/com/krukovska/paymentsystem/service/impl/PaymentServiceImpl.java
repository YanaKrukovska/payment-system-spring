package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.Payment;
import com.krukovska.paymentsystem.persistence.model.PaymentStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedList;

import static java.util.Objects.requireNonNull;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountService accountService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, AccountService accountService) {
        this.paymentRepository = paymentRepository;
        this.accountService = accountService;
    }

    @Override
    public Page<Payment> findAllClientPayments(Long clientId, PageRequest page) {
        requireNonNull(clientId, "Client id must be not null");
        requireNonNull(page, "Client page must be not null");

        return paymentRepository.findAllByAccountClientId(clientId, page);
    }

    @Override
    public Payment create(Payment payment) {
        requireNonNull(payment, "Payment must be not null");
        return paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public Response<Payment> send(long paymentId) {
        var payment = paymentRepository.findById(paymentId);

        if (payment == null) {
            return new Response<>(null, Collections.singletonList("Payment doesn't exist"));
        }

        payment.setStatus(PaymentStatus.SENT);
        paymentRepository.save(payment);

        accountService.withdrawAmount(payment.getAccount().getId(), payment.getAmount());
        return new Response<>(payment, new LinkedList<>());
    }

}
