package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;


    @GetMapping("/clients")
    public Set<ClientDTO> getAllClientsDTO(){
        return clientService.getAllClientsDTO();
    }

    @GetMapping("clients/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id){
        return clientService.getClientDTO(id);

    }


    @GetMapping("/clients/current")
    public ClientDTO getAuthenticatedClient(Authentication authentication){
        return clientService.getAuthenticatedClient(authentication);

    }



    @PostMapping("/clients")
    public ResponseEntity<Object> registerNewClient(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){


        if (firstName.isBlank()) {
            return new ResponseEntity<>("the firstName is missing", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()) {
            return new ResponseEntity<>("the lastName is missing", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()) {
            return new ResponseEntity<>("the email is missing", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()) {
            return new ResponseEntity<>("the password is missing", HttpStatus.FORBIDDEN);
        }

        if(clientService.findByEmail(email) != null){
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        Client client = new Client(firstName,
                lastName,
                email,
                passwordEncoder.encode(password));


        Set<AccountDTO> accountNumbersExisting = accountService.getAllAccountsDTO();

        Account accountNew = new Account( LocalDate.now(), 0);

        accountService.saveAccount(accountNew);
        client.addAccount(accountNew);
        clientService.saveClient(client);
        return new ResponseEntity<>(HttpStatus.CREATED);//cliente creado
    }






}