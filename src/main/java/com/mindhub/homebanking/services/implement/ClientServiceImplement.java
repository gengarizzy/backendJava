package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImplement implements ClientService{

    @Autowired
    ClientRepository clientRepository;


    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}
