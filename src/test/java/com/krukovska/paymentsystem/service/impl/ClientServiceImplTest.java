package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.persistence.model.ClientStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl service;

    @Mock
    private ClientRepository repo;

    @Test
    void findByIdSuccess() {
        service.findClientById(1L);
        verify(repo, times(1)).findClientById(1L);
    }

    @Test
    void findAllClientsSuccess() {
        service.findAllClients(Optional.of(1), Optional.of(3));
        verify(repo, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void findAllClientsSuccessNoPageParams() {
        service.findAllClients(Optional.empty(), Optional.empty());
        verify(repo, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void blockClientSuccess() {
        Client client = new Client();
        client.setStatus(ClientStatus.ACTIVE);
        when(repo.findClientById(anyLong())).thenReturn(client);

        service.updateClientStatus(1L, false);

        verify(repo, times(1)).findClientById(anyLong());
        verify(repo, times(1)).save(client);
        assertThat(client.getStatus(), equalTo(ClientStatus.BLOCKED));
    }

    @Test
    void unblockClientSuccess() {
        Client client = new Client();
        client.setStatus(ClientStatus.BLOCKED);
        when(repo.findClientById(anyLong())).thenReturn(client);

        service.updateClientStatus(1L, true);

        verify(repo, times(1)).findClientById(anyLong());
        verify(repo, times(1)).save(client);
        assertThat(client.getStatus(), equalTo(ClientStatus.ACTIVE));
    }

    @Test
    void blockClientNotExists() {
        when(repo.findClientById(anyLong())).thenReturn(null);
        Response<Client> result = service.updateClientStatus(1L, false);

        verify(repo, times(1)).findClientById(anyLong());
        verify(repo, never()).save(any());
        assertThat(result.getErrors().get(0), equalTo("Client doesn't exist"));
    }


}