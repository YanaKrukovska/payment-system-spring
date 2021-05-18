package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PaymentRepository extends PagingAndSortingRepository<Payment, Long> {

    List<Payment> findAllByAccountClient(Client client, Pageable pageable);

}
