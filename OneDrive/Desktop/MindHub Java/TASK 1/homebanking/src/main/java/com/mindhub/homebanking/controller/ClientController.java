package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.RoleType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

@RequestMapping("/api")
@RestController
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/clients")
    public Set<ClientDTO> getClientsDTO(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(toSet());
    }

    @RequestMapping("clients/{id}")
    public ClientDTO getClientDTO(@PathVariable Long id){
        return clientRepository.findById(id)
                .map(ClientDTO::new)
                .orElse(null);

    }


    @PostMapping("/clients")
    public ResponseEntity<Object> register
            ( //SOLICITO TENER firstName, lastName, email y password, para poder crear la cuenta
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password){


        //VERIFICACIONES DE CAMPOS VALIDOS Y EMAIL SIN REGISTRAR PREVIAMENTE
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

        if(clientRepository.findByEmail(email) != null){
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        //Si no se cumple ninguno de los if anteriores, es decir, todos los datos son correctos y no
        //se repite el meil, se procede a crear un nuevo Client, al que se le asocian los datos introducidos
        //y un rol de CLIENT automatico. Quiero solo poder crear CLIENT desde index

        Client client =  clientRepository.save(new Client(firstName,
                lastName,
                email,
                passwordEncoder.encode(password),
                RoleType.CLIENT));



        //Al crear el cliente, tambien quiero generarle una cuenta con la fecha del dia y balance 0
        Account accountNew = new Account(LocalDate.now(), 0); //Instancio una nueva cuenta
        accountRepository.save(accountNew); // Guardo la cuenta
        client.addAccount(accountNew); // La vinculo con el cliente
        clientRepository.save(client); // Una vez que guarde y vincule la cuenta al cliente, guardo el cliente
        return new ResponseEntity<>("Client/Account created",HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getAuthenticatedClient(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));

    }





}