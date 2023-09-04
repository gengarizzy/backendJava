package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountService accountService;


    @GetMapping("/accounts")
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(AccountDTO::new).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    //ESTA FUE LA SOLUCION DE LA TASK8
    //Como me daba error el method getData, el cual apunta a "/clients/current/accounts", lo que hice fue
    //generar una funcion de verbo GET a esa ruta, que entregue las cuentas del client autorizado
    @GetMapping("/clients/current/accounts")
    public ResponseEntity<Set<AccountDTO>> getClientAccounts(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client != null) {
            Set<AccountDTO> accountDTOs = client.getAccounts()
                    .stream()
                    .map(AccountDTO::new)
                    .collect(Collectors.toSet());
            return ResponseEntity.ok(accountDTOs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }







    //CREACION DE CUENTAS
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
        Account accountNew = new Account(newAccountDate, newAccountBalance);
        //No necesito asignar un numero de cuenta, ya que mi clase Account lo hace automaticamente de manera
        //incremental, utilizando un contador

        accountRepository.save(accountNew); //Guardo la nueva cuenta
        client.addAccount(accountNew); // La vinculo al cliente
        clientRepository.save(client); //Guardo el cliente, con la cuenta ya vinculada

        return new ResponseEntity<>("Cuenta agregada al cliente",HttpStatus.CREATED);
    }


}