package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.krukovska.paymentsystem.util.ModelHelper.setPaginationAttributes;

@Controller
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/all")
    public String getAllClients(Model model, @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size) {
        Page<Client> clientPage = clientService.findAllClients(page, size);
        model.addAttribute("clientPage", clientPage);
        setPaginationAttributes(model, page, clientPage);
        return "clients";
    }

    @PostMapping("/block")
    public String blockClient(@RequestParam String clientId) {
        clientService.updateClientStatus(clientId, false);
        return "redirect:/client/all";
    }

    @PostMapping("/unblock")
    public String unblockClient(@RequestParam String clientId) {
        clientService.updateClientStatus(clientId, true);
        return "redirect:/client/all";
    }
}
