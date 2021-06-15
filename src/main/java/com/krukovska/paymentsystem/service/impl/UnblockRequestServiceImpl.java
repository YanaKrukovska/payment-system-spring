package com.krukovska.paymentsystem.service.impl;

import com.krukovska.paymentsystem.persistence.model.AccountStatus;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;
import com.krukovska.paymentsystem.persistence.repository.RequestRepository;
import com.krukovska.paymentsystem.service.AccountService;
import com.krukovska.paymentsystem.service.ClientService;
import com.krukovska.paymentsystem.service.UnblockRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.DEFAULT_CURRENT_PAGE;
import static com.krukovska.paymentsystem.util.Constants.DEFAULT_PAGE_SIZE;

@Service
public class UnblockRequestServiceImpl implements UnblockRequestService {

    private final RequestRepository requestRepository;
    private final AccountService accountService;
    private final ClientService clientService;

    public UnblockRequestServiceImpl(RequestRepository requestRepository, AccountService accountService, ClientService clientService) {
        this.requestRepository = requestRepository;
        this.accountService = accountService;
        this.clientService = clientService;
    }

    @Override
    public UnblockRequest findRequestById(Long requestId) {
        return requestRepository.findUnblockRequestById(requestId);
    }

    @Override
    public Response<UnblockRequest> createNewRequest(Long accountId, Long clientId) {
        var account = accountService.findAccountById(accountId);

        if (account == null) {
            return new Response<>(null, Collections.singletonList("Account doesn't exist"));
        }

        var client = clientService.findClientById(clientId);
        if (client == null) {
            return new Response<>(null, Collections.singletonList("Client doesn't exist"));
        }

        var request = new UnblockRequest();
        request.setClient(client);
        request.setAccount(account);
        request.setCreationDate(LocalDate.now());

        return new Response<>(requestRepository.save(request), new LinkedList<>());
    }

    @Override
    public Page<UnblockRequest> findAllClientRequests(Long clientId, Optional<Integer> page, Optional<Integer> size) {
        return requestRepository.findAllByClientId(clientId,
                PageRequest.of(page.orElse(DEFAULT_CURRENT_PAGE) - 1, size.orElse(DEFAULT_PAGE_SIZE)));
    }

    @Override
    public Response<UnblockRequest> updateRequest(Long requestId, boolean isAccepted) {
        var request = findRequestById(requestId);
        if (request == null) {
            return new Response<>(null, Collections.singletonList("Request doesn't exist"));
        }
        request.setActionDate(LocalDate.now());

        if (isAccepted) {
            request.getAccount().setStatus(AccountStatus.ACTIVE);
        } else {
            return new Response<>(requestRepository.save(request), new LinkedList<>());
        }

        accountService.updateAccount(request.getAccount());
        return new Response<>(requestRepository.save(request), new LinkedList<>());
    }
}
