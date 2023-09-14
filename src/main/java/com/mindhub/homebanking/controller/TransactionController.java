package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;




    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createNewTransaction(
            Authentication authentication,
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber

    ) {

        //createNewTransaction necesita de un usuario autenticado,
        //monto, descripcion y cuentas de origen/destino


        // Validar que los parametros no sean nulos o vacios
        if (fromAccountNumber == null || fromAccountNumber.isBlank() ||
                toAccountNumber == null || toAccountNumber.isBlank() ||
                amount == null || amount <= 0 ||
                description == null || description.isBlank()) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }


        //INSTANCIO OBJETOS DE SUS RESPECTIVAS CLASES PARA REFERIRME AL CLIENTE AUTENTICADO Y LAS CUENTAS ORIGEN/DESTINO
        Client authorizedClient = clientService.findByEmail(authentication.getName());
        Account originAccount = accountService.findByNumber(fromAccountNumber);
        Account destinationAccount = accountService.findByNumber(toAccountNumber);


        //Uso el metodo .isBlank() para dar una condicion true/false a los if
        //Returns true if the string is empty or contains only white space codepoints, otherwise false.


        //VERIRIFACIONES DE CUENTAS

        //Verificar que los números de cuenta no sean iguales
        if (fromAccountNumber.equals(toAccountNumber)) {
            return new ResponseEntity<>("Choose another account to transfer", HttpStatus.FORBIDDEN);
        }


        //Verificar que exista la cuenta de origen
        if (fromAccountNumber.isBlank()) {
            return new ResponseEntity<>("No origin account selected", HttpStatus.FORBIDDEN);
        }


        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        if (accountService.findByNumber(fromAccountNumber) == null) {
            return new ResponseEntity<>("Origin account doesn't exists", HttpStatus.FORBIDDEN);
        }


        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        if(accountService.findByNumberAndClient(fromAccountNumber, authorizedClient) == null) {
            return new ResponseEntity<>("This account is not from this client", HttpStatus.FORBIDDEN);
        }

        //Verificar que exista la cuenta de destino
        if (toAccountNumber.isBlank()) {
            return new ResponseEntity<>("No destination account selected", HttpStatus.FORBIDDEN);
        }

        //Verificacion de monto, no tiene sentido transferir 0 unidades de moneda
        if(amount==0){
            return new ResponseEntity<>("You must indicate an amount", HttpStatus.FORBIDDEN);
        }

        //Vefico que el monto no sea negativo
        if(amount<0){
            return new ResponseEntity<>("Amount can't be negative", HttpStatus.FORBIDDEN);
        }



        //Verificar que la cuenta de origen tenga el monto disponible.
        if (accountService.findByNumber(fromAccountNumber).getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }


        //You must indicate a description
        //No se me pide una verificacion de descripcion, por lo que puede ser una transferencia por X motivo
        if (description.isBlank()) {
            return new ResponseEntity<>("You must indicate a description", HttpStatus.FORBIDDEN);

        }


        //PASADAS LAS VERIFICACIONES





        //Se deben crear dos transacciones,
        // una con el tipo de transacción “DEBIT” asociada a la cuenta de origen
        Transaction transactionDestination = new Transaction(TransactionType.CREDIT,
                amount, "Funds received from " +
                originAccount.getNumber(),
                LocalDateTime.now());


        // y la otra con el tipo de transacción “CREDIT” asociada a la cuenta de destino
        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT,
                -amount, "Funds sent to " +
                destinationAccount.getNumber(),
                LocalDateTime.now());
        //Recuerda que la transacción de tipo “DEBIT” debe quedar con el monto en negativo.



        // crear las transacciones asociandolas a las cuentas correspondientes
        destinationAccount.addTransaction(transactionDestination);
        originAccount.addTransaction(transactionOrigin);


        //por último guardarlas en el repositorio de transacciones
        transactionService.saveNewTransaction(transactionDestination);
        transactionService.saveNewTransaction(transactionOrigin);




        //Una vez realizada la creación de las transacciones, debes actualizar cada cuenta
        // con los montos correspondientes y guardarlas a través del repositorio de cuentas.

        //A la cuenta de origen se le restará el monto indicado en la petición
        originAccount.setBalance(originAccount.getBalance() - amount);

        // y a la cuenta de destino se le sumará el mismo monto
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);


        //GUARDO CADA CUENTA LUEGO DE AGREGAR LO DE LAS TRANSACTIONS
        accountService.saveAccount(destinationAccount);
        accountService.saveAccount(originAccount);


        //ENTREGAR CODIGO DE RESPUESTA DE CREATED. LA FUNCION REQUIERE UN RETURN
        return new ResponseEntity<>("Transaction created",HttpStatus.CREATED);



    }

}