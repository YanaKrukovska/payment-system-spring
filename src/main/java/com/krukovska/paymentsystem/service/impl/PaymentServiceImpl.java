package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.*;
import com.krukovska.paymentsystem.persistence.repository.PaymentRepository;
import com.krukovska.paymentsystem.service.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;

import static java.util.Objects.requireNonNull;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountService accountService;
    private final ClientService clientService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, AccountService accountService, ClientService clientService) {
        this.paymentRepository = paymentRepository;
        this.accountService = accountService;
        this.clientService = clientService;
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

    @Transactional(rollbackFor = PaymentProcessingException.class)
    @Override
    public Response<Payment> send(long paymentId) {
        var payment = paymentRepository.findById(paymentId);

        if (payment == null) {
            throw new PaymentProcessingException(("Payment with id " + paymentId + " does not exist"));
        }

        if (payment.getStatus() == PaymentStatus.SENT) {
            throw new PaymentProcessingException("Payment with id " + paymentId + " is already sent");
        }

        var account = accountService.findAccountById(payment.getAccount().getId());
        var client = clientService.findClientById(account.getClient().getId());
        if (client.getStatus() == ClientStatus.BLOCKED) {
            throw new PaymentProcessingException("Client with id " + client.getId() + " is blocked");
        }

        payment.setStatus(PaymentStatus.SENT);
        Response<Account> accountUpdateResponse = accountService.withdrawAmount(payment.getAccount().getId(),
                payment.getAmount());
        if (accountUpdateResponse.hasErrors()) {
            throw new PaymentProcessingException(accountUpdateResponse.getErrors().get(0));
        }

        paymentRepository.save(payment);

        return new Response<>(payment, new LinkedList<>());
    }

}
