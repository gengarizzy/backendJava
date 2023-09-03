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
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RequestMapping("/api")
@RestController
public class TransactionController {





    @Autowired
    ClientService clientService;  

    @Autowired
    AccountService accountService;


    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createNewTransaction(
            Authentication authentication,
            @RequestParam String originAccountNumber,
            @RequestParam  String destinationAccountNumber,
            @RequestParam Double amount,
            @RequestParam String description){


        Account fromAccountNumber=accountService.findByNumber(originAccountNumber);
        Account toAccountNumber=accountService.findByNumber(destinationAccountNumber);
        Client client=clientService.findByEmail(authentication.getName());





        //VERIFICACIONES. Si algun if se cumple, se entrega un FORBIDDEN Status

        if(description.isBlank()){
            return new ResponseEntity<>("Description missing.", HttpStatus.FORBIDDEN);
        }

        if(originAccountNumber.isBlank()){
            return new ResponseEntity<>("Origin account missing.", HttpStatus.FORBIDDEN);
        }

        if(destinationAccountNumber.isBlank()){
            return new ResponseEntity<>("Destination account missing.", HttpStatus.FORBIDDEN);
        }

        if (fromAccountNumber.equals(toAccountNumber)){
            return new ResponseEntity<>("You can't transfer to the same account.", HttpStatus.FORBIDDEN);
        }

        if(accountService.findByNumber(originAccountNumber)==null){
            return new ResponseEntity<>("Origin account doesn't exist.", HttpStatus.FORBIDDEN);
        }
        if(accountService.findByClient(client)==null){
            return new ResponseEntity<>("This account is not property of this client.", HttpStatus.FORBIDDEN);
        }
        if(accountService.findByNumber(destinationAccountNumber)==null){
            return new ResponseEntity<>("Destination account not registered.", HttpStatus.FORBIDDEN);
        }
        if(accountService.findByNumber(originAccountNumber).getBalance()<amount){
            return new ResponseEntity<>("Insufficient funds.", HttpStatus.FORBIDDEN);
        }
        if(amount==0){
            return new ResponseEntity<>("Amount missing. Please check amount input field.", HttpStatus.FORBIDDEN);
        }


        //SI NO SE CUMPLE NINGUN IF, ES DECIR, LAS VERIFICACIONES SON EXITOSAS, ENTONCES:
        else{



            Transaction transactionDebit=new Transaction
                    (TransactionType.DEBIT, //Pierde dinero
                            amount,
                            description,
                            LocalDateTime.now());

            Transaction transactionCredit=new Transaction
                    (TransactionType.CREDIT, //Gana dinero
                            amount,
                            description,
                            LocalDateTime.now());


            fromAccountNumber.addTransaction(transactionDebit);
            toAccountNumber.addTransaction(transactionCredit);


            transactionService.saveTransaction(transactionCredit);
            transactionService.saveTransaction(transactionDebit);


            fromAccountNumber.setBalance(fromAccountNumber.getBalance()-amount);
            toAccountNumber.setBalance(toAccountNumber.getBalance()+amount);


            accountService.saveAccount(fromAccountNumber);
            accountService.saveAccount(toAccountNumber);

            
            return new ResponseEntity<>("Successful transaction",HttpStatus.CREATED);
        }
    }



}
