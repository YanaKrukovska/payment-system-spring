package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.AccountStatus;
import com.krukovska.paymentsystem.persistence.model.Request;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.repository.RequestRepository;
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
public class RequestService {

    private final RequestRepository requestRepository;
    private final AccountService accountService;
    private final ClientService clientService;

    public RequestService(RequestRepository requestRepository, AccountServiceImpl accountService, ClientService clientService) {
        this.requestRepository = requestRepository;
        this.accountService = accountService;
        this.clientService = clientService;
    }

    public Request findRequestById(String requestId) {
        return requestRepository.findById(Long.valueOf(requestId)).orElse(null);
    }

    public Response<Request> createNewRequest(Long accountId, Long clientId) {
        var account = accountService.findAccountById(accountId);

        if (account == null) {
            return new Response<>(null, Collections.singletonList("Account doesn't exist"));
        }

        var client = clientService.findClientById(clientId);
        if (client == null) {
            return new Response<>(null, Collections.singletonList("Client doesn't exist"));
        }

        var request = new Request();
        request.setClient(client);
        request.setAccount(account);
        request.setCreationDate(new Date());

        return new Response<>(requestRepository.save(request), new LinkedList<>());
    }

    public Page<Request> findAllClientRequests(Long clientId, Optional<Integer> page, Optional<Integer> size) {
        return requestRepository.findAllByClientId(clientId,
                PageRequest.of(page.orElse(DEFAULT_CURRENT_PAGE) - 1, size.orElse(DEFAULT_PAGE_SIZE)));
    }

    public Response<Request> updateRequest(String requestId, boolean isAccepted) {
        var request = findRequestById(requestId);
        if (request == null) {
            return new Response<>(null, Collections.singletonList("Request doesn't exist"));
        }
        request.setActionDate(new Date());

        if (isAccepted) {
            request.getAccount().setStatus(AccountStatus.ACTIVE);
        } else {
            return new Response<>(requestRepository.save(request), new LinkedList<>());
        }

        accountService.updateAccount(request.getAccount());
        return new Response<>(requestRepository.save(request), new LinkedList<>());
    }
}
