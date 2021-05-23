package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.ClientStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.ClientRepository;
import com.krukovska.paymentsystem.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.DEFAULT_CURRENT_PAGE;
import static com.krukovska.paymentsystem.util.Constants.DEFAULT_PAGE_SIZE;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client findClientById(Long id) {
        return clientRepository.findClientById(id);
    }

    @Override
    public Page<Client> findAllClients(Optional<Integer> page, Optional<Integer> size) {
        return clientRepository.findAll(PageRequest.of(page.orElse(DEFAULT_CURRENT_PAGE) - 1,
                size.orElse(DEFAULT_PAGE_SIZE)));
    }

    @Override
    public Response<Client> updateClientStatus(Long clientId, boolean isBlocked) {
        var client = findClientById(clientId);
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
