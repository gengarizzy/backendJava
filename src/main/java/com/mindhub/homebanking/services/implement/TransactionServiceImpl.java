package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {


    @Autowired
    AccountService accountService;

    @Autowired
    ClientService clientService;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;



    @Override
    public ResponseEntity<Object> createNewTransaction(Authentication authentication,
                                                       Double amount, String description,
                                                       String fromAccountNumber, String toAccountNumber) {

        //createNewTransaction necesita de un usuario autenticado,
        //monto, descripcion y cuentas de origen/destino



        //INSTANCIO OBJETOS DE SUS RESPECTIVAS CLASES PARA REFERIRME AL CLIENTE AUTENTICADO Y LAS CUENTAS ORIGEN/DESTINO
        Client authorizedClient = clientRepository.findByEmail(authentication.getName());
        Account originAccount = accountRepository.findByNumber(fromAccountNumber);
        Account destinationAccount = accountRepository.findByNumber(toAccountNumber);


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
        if (accountRepository.findByNumber(fromAccountNumber) == null) {
            return new ResponseEntity<>("Origin account doesn't exists", HttpStatus.FORBIDDEN);
        }


        //Verificar que la cuenta de origen pertenezca al cliente autenticado
        if(accountRepository.findByNumberAndClient(fromAccountNumber, authorizedClient) == null) {
            return new ResponseEntity<>("This account is not from this client", HttpStatus.FORBIDDEN);
        }

        //Verificar que exista la cuenta de destino
        if (toAccountNumber.isBlank()) {
            return new ResponseEntity<>("No destination account selected", HttpStatus.FORBIDDEN);
        }


        //Verificar que la cuenta de origen tenga el monto disponible.
        if (accountRepository.findByNumber(fromAccountNumber).getBalance() < amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }


        //You must indicate a description
        //No se me pide una verificacion de descripcion, por lo que puede ser una transferencia por X motivo
        if (description.isBlank()) {
            return new ResponseEntity<>("You must indicate a description", HttpStatus.FORBIDDEN);

        }
        //Verificacion de monto, no tiene sentido transferir 0 unidades de moneda
        if(amount==0){
            return new ResponseEntity<>("You must indicate an amount", HttpStatus.FORBIDDEN);
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
        originAccount.addTransaction(transactionOrigin);
        destinationAccount.addTransaction(transactionDestination);

        //por último guardarlas en el repositorio de transacciones
        transactionRepository.save(transactionDestination);
        transactionRepository.save(transactionOrigin);




        //Una vez realizada la creación de las transacciones, debes actualizar cada cuenta
        // con los montos correspondientes y guardarlas a través del repositorio de cuentas.

        //A la cuenta de origen se le restará el monto indicado en la petición
        originAccount.setBalance(originAccount.getBalance() - amount);

        // y a la cuenta de destino se le sumará el mismo monto
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);


        //GUARDO CADA CUENTA LUEGO DE AGREGAR LO DE LAS TRANSACTIONS
        accountRepository.save(destinationAccount);
        accountRepository.save(originAccount);


        //ENTREGAR CODIGO DE RESPUESTA DE CREATED. LA FUNCION REQUIERE UN RETURN
        return new ResponseEntity<>("Transaction created",HttpStatus.CREATED);



    }

    @Override
    public void saveNewTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }


}
