package com.mindhub.homebanking.services;
import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Set;

public interface AccountService {


    void saveAccount(Account account);

    ResponseEntity<AccountDTO> getAccountById(Long id);

    ResponseEntity<Set<AccountDTO>> getClientAccounts(Authentication authentication);


    ResponseEntity<Object> createAccount(Authentication authentication);


    AccountDTO getAccountDTO(@PathVariable Long id);

    Set<AccountDTO> getAllAccountsDTO();

    Account findByNumber(String destinationAccountNumber);
}
