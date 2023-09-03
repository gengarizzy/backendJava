package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;

public interface ClientService {

    Client findByEmail(String email);

}
