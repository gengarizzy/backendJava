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
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional //Agrego Transactional porque quiero revertir la creacion en caso de error
    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        Client client = clientService.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("No puedes tener más de 3 cuentas", HttpStatus.FORBIDDEN);
        }



        Account newAccount = accountService.createNewDefaultAccount();
        //ESTE METODO REEMPLAZA AL CODIGO COMENTADO DEBAJO. De esa manera, paso la logica al servicio, la cual
        //utilizo mediante un metodo con un nombre autodescriptivo
        //crear una nueva cuenta por defecto, refiriendose a un balance 0 y fecha del dia

        //Este metodo solo se encarga de una cosa --> crear una nueva cuenta por defecto

//        LocalDate newAccountDate = LocalDate.now();
//        Double newAccountBalance = 0.0;
//        Account accountNew = new Account(newAccountDate, newAccountBalance);


        //Aca no puedo simplificar mas, no puedo tener un metodo que guarde la cuenta, la agregue al cliente
        //y guarde el cliente con esa modificacion, porque estaria violando el Single Responsability Principle
        accountService.saveAccount(newAccount);
        client.addAccount(newAccount);
        clientService.saveClient(client);
        //DUDA: ese enfoque es correcto?
        //MOTIVO: Si bien violaria el SRP, son acciones destinadas a una cosa (guardar la cuenta a un cliente),
        //Quizas podria pasarlo a un metodo privado en esta clase (para modificarlo en caso necesario), por ejemplo,
        // private void saveAccountToClient();

        return new ResponseEntity<>("Cuenta agregada al cliente", HttpStatus.CREATED);

    }



    @Transactional
    @DeleteMapping("/clients/current/accounts/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, Authentication authentication){

        try{
            accountService.deleteAccount(authentication, id);

            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception exception){

            return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);

        }



    }




}