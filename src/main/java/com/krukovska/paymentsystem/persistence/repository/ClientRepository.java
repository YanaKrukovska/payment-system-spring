package com.krukovska.paymentsystem.persistence.repository;

import com.krukovska.paymentsystem.persistence.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {

     Client findClientById(Long id);

}
