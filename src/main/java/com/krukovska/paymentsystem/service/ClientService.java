package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.AccountStatus;
import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.ClientStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.DEFAULT_CURRENT_PAGE;
import static com.krukovska.paymentsystem.util.Constants.DEFAULT_PAGE_SIZE;

@Service
//TODO add interface
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client findClientById(Long id) {
        return clientRepository.findClientById(id);
    }

    public Page<Client> findAllClients(Optional<Integer> page, Optional<Integer> size) {
        return clientRepository.findAll(PageRequest.of(page.orElse(DEFAULT_CURRENT_PAGE) - 1,
                size.orElse(DEFAULT_PAGE_SIZE)));
    }

    public Response<Client> updateClientStatus(String clientId, boolean isBlocked) {
        var client = findClientById(Long.valueOf(clientId));
        if (client == null) {
            return new Response<>(null, Collections.singletonList("Client doesn't exist"));
        }

        if (isBlocked) {
            client.setStatus(ClientStatus.ACTIVE);
        } else {
            client.setStatus(ClientStatus.BLOCKED);
        }

        return new Response<>(clientRepository.save(client), new LinkedList<>());
    }
}
