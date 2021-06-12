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

import java.util.LinkedList;
import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.DEFAULT_CURRENT_PAGE;
import static com.krukovska.paymentsystem.util.Constants.DEFAULT_PAGE_SIZE;
import static com.krukovska.paymentsystem.util.SortHelper.buildSort;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountService accountService;

    public PaymentServiceImpl(PaymentRepository paymentRepository, AccountService accountService) {
        this.paymentRepository = paymentRepository;
        this.accountService = accountService;
    }

    @Override
    public Page<Payment> findAllClientPayments(Long clientId, Optional<Integer> page, Optional<Integer> size,
                                               Optional<String> sortField, Optional<String> sortDirection) {
        return paymentRepository.findAllByAccountClientId(clientId,
                PageRequest.of(page.orElse(DEFAULT_CURRENT_PAGE) - 1, size.orElse(DEFAULT_PAGE_SIZE),
                        buildSort(sortField, sortDirection)));
    }

    @Override
    public Payment create(Payment payment) {
        //TODO: add validation
        return paymentRepository.save(payment);
    }

    @Transactional
    @Override
    public Response<Payment> send(long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        payment.setStatus(PaymentStatus.SENT);
        paymentRepository.save(payment);
        accountService.withdrawAmount(payment.getAccount().getId(), payment.getAmount());
        return new Response<>(payment, new LinkedList<>());
    }

}