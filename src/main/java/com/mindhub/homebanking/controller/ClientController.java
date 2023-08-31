    package com.mindhub.homebanking.controller;
    import com.mindhub.homebanking.DTO.ClientDTO;
    import com.mindhub.homebanking.models.Account;
    import com.mindhub.homebanking.models.Client;
    import com.mindhub.homebanking.models.RoleType;
    import com.mindhub.homebanking.repositories.AccountRepository;
    import com.mindhub.homebanking.repositories.ClientRepository;
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

    import static java.util.stream.Collectors.toSet;

    @RequestMapping("/api")
    @RestController
    public class ClientController {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private ClientRepository clientRepository;

        @Autowired
        private ClientService clientService;

        @Autowired
        private AccountRepository accountRepository;

        @GetMapping("/clients")
        public Set<ClientDTO> getClientsDTO(){
           return clientService.getClientsDTO();
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

            return clientService.registerNewClient(firstName, lastName, email, password);

        }






    }