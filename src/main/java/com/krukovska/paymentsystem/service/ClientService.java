package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.repository.ClientRepository;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client findClientById(Long id){
        return clientRepository.findClientById(id);
    }
}
