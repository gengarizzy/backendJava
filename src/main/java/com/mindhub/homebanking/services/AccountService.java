package com.mindhub.homebanking.services;
import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

public interface AccountService {

    ResponseEntity<Object> createAccount(Client client);

    AccountDTO getAccountDTO(@PathVariable Long id);

    Set<AccountDTO> getAccountDTO();

}
