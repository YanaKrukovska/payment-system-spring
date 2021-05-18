package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.model.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

}
