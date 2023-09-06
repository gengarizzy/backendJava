package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;

public interface TransactionService {

    ResponseEntity<Object> createNewTransaction(Authentication authentication,
                                                Double amount,
                                                String description,
                                                String fromAccountNumber,
                                                String toAccountNumber);

    void saveNewTransaction(Transaction transaction);


}