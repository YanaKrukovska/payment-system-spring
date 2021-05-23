package com.krukovska.paymentsystem.service;

import com.krukovska.paymentsystem.persistence.model.UnblockRequest;
import com.krukovska.paymentsystem.persistence.model.Response;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UnblockRequestService {

    UnblockRequest findRequestById(Long requestId);

    Response<UnblockRequest> createNewRequest(Long accountId, Long clientId);

    Page<UnblockRequest> findAllClientRequests(Long clientId, Optional<Integer> page, Optional<Integer> size);

    Response<UnblockRequest> updateRequest(Long requestId, boolean isAccepted);

}
