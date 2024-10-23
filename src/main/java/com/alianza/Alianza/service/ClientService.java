package com.alianza.Alianza.service;

import com.alianza.Alianza.entity.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final List<Client> clients = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();
    private AtomicInteger businessIdCounter = new AtomicInteger(1);

    public ClientService() {
        // Datos de ejemplo
        clients.add(new Client() {{
            setId(counter.incrementAndGet());
            setSharedKey("jgutierrez");
            setBusinessId("Julian Gutierrez");
            setEmail("jgutierrez@example.com");
            setPhone("3219876543");
            setDateAdded(LocalDate.of(2019, 5, 20));
        }});
        clients.add(new Client() {{
            setId(counter.incrementAndGet());
            setSharedKey("mmartinez");
            setBusinessId("Maria Martinez");
            setEmail("mmartinez@example.com");
            setPhone("3219876543");
            setDateAdded(LocalDate.of(2019, 5, 20));
        }});
        logger.info("ClientService initialized with {} sample clients", clients.size());
    }

    public List<Client> getClients() {
        logger.info("Fetching all clients...");
        return new ArrayList<>(clients);
    }

    public List<Client> searchClients(String sharedKey) {
        logger.info("Searching clients with sharedKey: {}", sharedKey);
        List<Client> result = clients.stream()
                .filter(client -> client.getSharedKey().toLowerCase().contains(sharedKey.toLowerCase()))
                .collect(Collectors.toList());
        logger.info("Search completed. Found {} clients for sharedKey: {}", result.size(), sharedKey);
        return result;
    }

    public List<Client> advancedSearch(String businessId, String phone, String email, String dateAdded, String endDate) {
        logger.info("Performing advanced search with businessId: {}, phone: {}, email: {}, dateAdded: {}, endDate: {}",
                businessId, phone, email, dateAdded, endDate);

        List<Client> result = clients.stream()
                .filter(client -> (businessId == null || client.getBusinessId().equalsIgnoreCase(businessId)))
                .filter(client -> (phone == null || client.getPhone().equalsIgnoreCase(phone)))
                .filter(client -> (email == null || client.getEmail().equalsIgnoreCase(email)))
                .filter(client -> (dateAdded == null || client.getDateAdded().isEqual(LocalDate.parse(dateAdded))))
                .filter(client -> (endDate == null || client.getEndDate().isEqual(LocalDate.parse(endDate))))
                .collect(Collectors.toList());

        logger.info("Advanced search found {} clients", result.size());
        return result;
    }

    public Client createClient(Client client) {
        logger.info("Creating new client: {}", client);
        String sharedKey = generateSharedKey(client.getBusinessId());
        client.setSharedKey(sharedKey);
        client.setBusinessId(client.getBusinessId());
        clients.add(client);
        logger.info("Client created with ID: {}", client.getId());
        return client;
    }

    private String generateSharedKey(String name) {
        String[] nameParts = name.trim().split("\\s+");
        if (nameParts.length >= 2) {
            return (nameParts[0].charAt(0) + nameParts[1]).toLowerCase();
        } else {
            return name.toLowerCase();
        }
    }
}
