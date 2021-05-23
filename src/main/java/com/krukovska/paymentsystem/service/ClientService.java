package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.Response;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ClientService {

    Client findClientById(Long id);

    Page<Client> findAllClients(Optional<Integer> page, Optional<Integer> size);

    Response<Client> updateClientStatus(Long clientId, boolean isBlocked);

}
