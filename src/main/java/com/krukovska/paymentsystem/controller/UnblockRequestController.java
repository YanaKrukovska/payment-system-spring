package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.persistence.model.UnblockRequest;
import com.krukovska.paymentsystem.persistence.model.User;
import com.krukovska.paymentsystem.service.impl.UnblockRequestServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.krukovska.paymentsystem.util.ModelHelper.setPaginationAttributes;

@Controller
@RequestMapping("/request")
public class UnblockRequestController {

    private final UnblockRequestServiceImpl requestService;

    public UnblockRequestController(UnblockRequestServiceImpl requestService) {
        this.requestService = requestService;
    }

    @GetMapping(value = "/all")
    public String allClientRequests(Model model, @RequestParam("page") Optional<Integer> page,
                                    @RequestParam("size") Optional<Integer> size) {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page<UnblockRequest> reqPage;
        if (user.getRole().getName().equals("ROLE_ADMIN")) {
            reqPage = requestService.findAllRequests(page, size);
        } else {
            reqPage = requestService.findAllClientRequests(user.getClient().getId(), page, size);
        }
        model.addAttribute("requestPage", reqPage);
        setPaginationAttributes(model, page, reqPage);
        return "requests";
    }

    @PostMapping("/add/{accountId}")
    public String createRequest(Model model, @PathVariable Long accountId) {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Response<UnblockRequest> createResponse = requestService.createNewRequest(accountId, user.getClient().getId());
        if (!createResponse.isOkay()) {
            model.addAttribute("errors", createResponse.getErrors());
            return "redirect:/account/all";
        }

        return "redirect:/request/all";
    }

    @PostMapping("/accept/{requestId}")
    public String acceptRequest(@PathVariable String requestId) {
        requestService.updateRequest(Long.valueOf(requestId), true);
        return "redirect:/request/all";
    }

}
