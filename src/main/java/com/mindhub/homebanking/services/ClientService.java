package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public interface ClientService {

    ResponseEntity<Object> registerNewClient(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password);


    Client findByEmail(String email);

    void saveClient(Client client);





    Set<ClientDTO> getAllClientsDTO();

    ClientDTO getClientDTO(@PathVariable Long id);

    ClientDTO getAuthenticatedClient(Authentication authentication);


}
