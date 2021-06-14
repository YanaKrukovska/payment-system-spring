package com.krukovska.paymentsystem.controller;

import com.krukovska.paymentsystem.persistence.model.Client;
import com.krukovska.paymentsystem.service.ClientService;
import com.krukovska.paymentsystem.service.impl.ClientServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{clientId}/block")
    public String blockClient(@PathVariable Long clientId) {
        clientService.updateClientStatus(clientId, false);
        return "redirect:/client/all";
    }

    @PostMapping("/{clientId}/unblock")
    public String unblockClient(@PathVariable Long clientId) {
        clientService.updateClientStatus(clientId, true);
        return "redirect:/client/all";
    }
}
