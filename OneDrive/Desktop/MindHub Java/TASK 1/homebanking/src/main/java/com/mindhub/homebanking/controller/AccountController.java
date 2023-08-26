package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/accounts")
    public Set<AccountDTO> getAccountsDTO() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccountDTO(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(AccountDTO::new)
                .orElse(null);
    }





    //CREACION DE CUENTAS
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());
        //Uso findByEmail ya que mi sesion depende de autenticar al usuario con X email




        //Defino un limite de 3 cuentas. Si el getter de Accounts supera las 3 accounts, deniega la solicitud
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("No puedes tener mas de 3 cuentas", HttpStatus.FORBIDDEN);
        }





        //--------------------------CREACION DE NUEVA CUENTA------------------------

        //Defino los valores del numero de cuenta, fecha y balance que van a tener las nuevas cuentas

        LocalDate newAccountDate = LocalDate.now();
        Double newAccountBalance = 0.0;


        //Instancio el objeto accountNew de la clase Account, con los parametros definidos arriba
        Account accountNew = new Account( newAccountDate, newAccountBalance);
        //No necesito asignar un numero de cuenta, ya que mi clase Account lo hace automaticamente de manera
        //incremental, utilizando un contador

        accountRepository.save(accountNew); //Guardo la nueva cuenta
        client.addAccount(accountNew); // La vinculo al cliente
        clientRepository.save(client); //Guardo el cliente, con la cuenta ya vinculada

        return new ResponseEntity<>("Cuenta agregada al cliente",HttpStatus.CREATED);
    }
}