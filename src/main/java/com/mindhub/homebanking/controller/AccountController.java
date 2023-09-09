package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;


    @GetMapping("/accounts")
    public Set<AccountDTO> getAllAccountsDTO() {

        return accountService.getAllAccountsDTO();
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable long id) {
        return accountService.getAccountById(id);
    }

    //ESTA FUE LA SOLUCION DE LA TASK8
    //Como me daba error el method getData, el cual apunta a "/clients/current/accounts", lo que hice fue
    //generar una funcion de verbo GET a esa ruta, que entregue las cuentas del client autorizado
    @GetMapping("/clients/current/accounts")
    public ResponseEntity<Set<AccountDTO>> getClientAccounts(Authentication authentication) {

        return accountService.getClientAccounts(authentication);
    }


    //CREACION DE CUENTAS
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("No puedes tener más de 3 cuentas", HttpStatus.FORBIDDEN);
        }

        LocalDate newAccountDate = LocalDate.now();
        Double newAccountBalance = 0.0;

        Account accountNew = new Account(newAccountDate, newAccountBalance);

        accountService.saveAccount(accountNew);
        client.addAccount(accountNew);
        clientService.saveClient(client);

        return new ResponseEntity<>("Cuenta agregada al cliente", HttpStatus.CREATED);

    }


}