package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Request;
import com.krukovska.paymentsystem.persistence.model.Response;
import com.krukovska.paymentsystem.service.RequestService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.krukovska.paymentsystem.util.Constants.CLIENT_ID;
import static com.krukovska.paymentsystem.util.ModelHelper.setPaginationAttributes;

@Controller
@RequestMapping("/request")
//TODO rename to more meaningful name
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping(value = "/all")
    public String allClientRequests(Model model, @RequestParam("page") Optional<Integer> page,
                                    @RequestParam("size") Optional<Integer> size) {

        Page<Request> reqPage = requestService.findAllClientRequests(CLIENT_ID, page, size);
        model.addAttribute("requestPage", reqPage);
        setPaginationAttributes(model, page, reqPage);
        return "requests";
    }

    @PostMapping("/add")
    public String createRequest(Model model, @RequestParam Long accountId) {
        Response<Request> createResponse = requestService.createNewRequest(accountId, CLIENT_ID);
        if (!createResponse.isOkay()) {
            model.addAttribute("errors", createResponse.getErrors());
            return "redirect:/account/all";
        }

        return "redirect:/request/all";
    }

    @PostMapping("/accept")
    public String acceptRequest(@RequestParam String requestId) {
        requestService.updateRequest(requestId, true);
        return "redirect:/request/all";
    }

    @PostMapping("/decline")
    public String declineRequest(@RequestParam String requestId) {
        requestService.updateRequest(requestId, false);
        return "redirect:/request/all";
    }
}
