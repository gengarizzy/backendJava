package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.RoleType;
import com.mindhub.homebanking.repositories.ClientRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static com.mindhub.homebanking.models.RoleType.CLIENT;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    //Para registrar, se pide nombre,apellido,email y contrasena
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            //Si nombre,apellido,email o contrasena estan vacios, se deniega el acceso por falta de data

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }

        if (clientRepository.findByEmail(email) !=  null) {
            //Si ya se encuentra un email asignado en la base de datos mediante el repositorio, se
            //prohibe el registro ya que email y client tienen una relacion de 1 a 1

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }
    //si se cumplen los requisitos, se guarda el nuevo cliente en la db mediante el repositorio
        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password),CLIENT));

        return new ResponseEntity<>(HttpStatus.CREATED);//cliente creado

    }

    @RequestMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){

        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));

    }

    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){

        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);

    }

}