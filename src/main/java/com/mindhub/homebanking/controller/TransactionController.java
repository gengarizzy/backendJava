package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.implement.ClientServiceImpl;
import com.mindhub.homebanking.services.implement.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionServiceImpl transactionService;

    @Autowired
    ClientServiceImpl clientService;



    @Transactional
    @PostMapping("/clients/current/transactions")
    public ResponseEntity<?> createTransaction(Authentication authentication,
                                               @RequestParam("origin") String originAccountNumber,
                                               @RequestParam("destination") String destinationAccountNumber,
                                               @RequestParam Double amount){


        if(originAccountNumber == null) return new ResponseEntity<>("Missing origin account number", HttpStatus.FORBIDDEN);

        if(destinationAccountNumber == null) return new ResponseEntity<>("Missing destination account number", HttpStatus.FORBIDDEN);

        if(amount == null) return new ResponseEntity<>("Missing transacion amount", HttpStatus.FORBIDDEN);

        if(amount < 1) return new ResponseEntity<>("You can't make 0 currency transactions", HttpStatus.FORBIDDEN);

        try{

            transactionService.createTransaction(authentication, originAccountNumber, destinationAccountNumber, amount);

            return new ResponseEntity<>(HttpStatus.CREATED);

        }catch(Exception exception){

            return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);

        }


    }




}
