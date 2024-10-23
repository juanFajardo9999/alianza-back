package com.alianza.Alianza;

import com.alianza.Alianza.controller.ClientController;
import com.alianza.Alianza.entity.Client;
import com.alianza.Alianza.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @InjectMocks
    private ClientController clientController;

    @Mock
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClientsWithoutSharedKey() {
        when(clientService.getClients()).thenReturn(List.of(
                new Client() {{
                    setId(1L);
                    setSharedKey("jgutierrez");
                    setBusinessId("123456");
                    setEmail("jgutierrez@example.com");
                    setPhone("3219876543");
                    setDateAdded(LocalDate.of(2019, 5, 20));
                }}
        ));

        List<Client> clients = clientController.getClients(null);
        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    @Test
    void testAdvancedSearchByEmail() {
        String email = "jgutierrez@example.com";

        when(clientService.advancedSearch(null, null, email, null, null))
                .thenReturn(List.of(new Client() {{
                    setId(1L);
                    setSharedKey("jgutierrez");
                    setBusinessId("123456");
                    setEmail("jgutierrez@example.com");
                    setPhone("3219876543");
                    setDateAdded(LocalDate.of(2019, 5, 20));
                }}));

        List<Client> clients = clientController.advancedSearch(null, null, email, null, null);
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

        when(clientService.createClient(newClient)).thenReturn(newClient);

        ResponseEntity<Client> response = clientController.createClient(newClient);
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("john.doe@example.com", response.getBody().getEmail());
    }
}
