package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ResponseEntity<Object> createAccount(Client authorizedClient) {
        if (authorizedClient.getAccounts().size() >= 3) {
            return new ResponseEntity<>("No puedes tener más de 3 cuentas", HttpStatus.FORBIDDEN);
        }

        LocalDate newAccountDate = LocalDate.now();
        Double newAccountBalance = 0.0;

        Account accountNew = new Account(newAccountDate, newAccountBalance);

        accountRepository.save(accountNew);
        authorizedClient.addAccount(accountNew);
        clientRepository.save(authorizedClient);

        return new ResponseEntity<>("Cuenta agregada al cliente", HttpStatus.CREATED);
    }


    //Este metodo recibo una account especifica mediante ID
    @Override
    public AccountDTO getAccountDTO(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(AccountDTO::new)
                .orElse(null);
    }

    //Este metodo recibe todas las accounts
    @Override
    public Set<AccountDTO> getAccountDTO() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }


}
