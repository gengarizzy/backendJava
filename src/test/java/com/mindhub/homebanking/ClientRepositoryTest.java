package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientRepositoryTest {

    @Autowired
    ClientRepository clientRepository;

    @Test
    public void existClients(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients,is(not(empty())));
    }

    @Test
    public void shouldExistPersonalAccountForNonAdminClients() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            // Verifica si el cliente no es el administrador (admin@admin.com)
            if (!"admin@admin.com".equals(client.getEmail())) {
                // Verifica que el cliente tenga al menos una cuenta sin permisos de admin
                assertThat("Personal account should exist for client: " + client.getEmail(),
                        client.getAccounts().size(), greaterThan(0));
            }
        }
    }



}
