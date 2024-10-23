package com.alianza.Alianza.controller;

import com.alianza.Alianza.entity.Client;
import com.alianza.Alianza.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClientController {

    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Client> getClients(@RequestParam(required = false) String sharedKey) {
        logger.info("GET request received for fetching clients with sharedKey: {}", sharedKey);
        List<Client> clients;
        try {
            if (sharedKey != null) {
                clients = clientService.searchClients(sharedKey);
                logger.info("Clients fetched successfully for sharedKey: {}", sharedKey);
            } else {
                clients = clientService.getClients();
                logger.info("All clients fetched successfully.");
            }
            return clients;
        } catch (Exception e) {
            logger.error("Error fetching clients: ", e);
            throw e;
        }
    }

    @GetMapping("/search")
    public List<Client> advancedSearch(
            @RequestParam(required = false) String businessId,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String dateAdded,
            @RequestParam(required = false) String endDate) {

        logger.info("GET request received for advanced search with params - businessId: {}, phone: {}, email: {}, dateAdded: {}, endDate: {}",
                businessId, phone, email, dateAdded, endDate);
        try {
            List<Client> clients = clientService.advancedSearch(businessId, phone, email, dateAdded, endDate);
            logger.info("Advanced search completed successfully. {} clients found.", clients.size());
            return clients;
        } catch (Exception e) {
            logger.error("Error performing advanced search: ", e);
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        logger.info("POST request received for creating client: {}", client);
        try {
            Client createdClient = clientService.createClient(client);
            logger.info("Client created successfully with id: {}", createdClient.getId());
            return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating client: ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
