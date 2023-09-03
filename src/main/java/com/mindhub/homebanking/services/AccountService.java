package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

public interface AccountService {


    Account findByNumber(String number);

    Account findByClient(Client client);

    void saveAccount(Account account);



}
