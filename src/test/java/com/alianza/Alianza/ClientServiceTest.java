package com.alianza.Alianza;

import com.alianza.Alianza.entity.Client;
import com.alianza.Alianza.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientService();
    }

    @Test
    void testGetClients() {
        List<Client> clients = clientService.getClients();
        assertNotNull(clients);
        assertEquals(2, clients.size()); // Basado en los datos de ejemplo
    }

    @Test
    void testSearchClients() {
        String sharedKey = "jgutierrez";
        List<Client> clients = clientService.searchClients(sharedKey);
        assertEquals(1, clients.size());
        assertEquals("jgutierrez", clients.get(0).getSharedKey());
    }

    @Test
    void testAdvancedSearchByEmail() {
        String email = "jgutierrez@example.com";
        List<Client> clients = clientService.advancedSearch(null, null, email, null, null);
        assertEquals(1, clients.size());
        assertEquals("jgutierrez@example.com", clients.get(0).getEmail());
    }

    @Test
    void testCreateClient() {
        Client newClient = new Client();
        newClient.setBusinessId("John Doe");
        newClient.setEmail("john.doe@example.com");
        newClient.setPhone("3219876543");
        newClient.setDateAdded(LocalDate.now());

        Client createdClient = clientService.createClient(newClient);
        assertNotNull(createdClient);
        assertEquals("jdoe", createdClient.getSharedKey()); // La clave compartida generada
        assertEquals("John Doe", createdClient.getBusinessId());
    }
}
