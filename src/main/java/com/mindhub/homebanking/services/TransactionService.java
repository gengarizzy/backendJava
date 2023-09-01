package com.mindhub.homebanking.services;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import javax.persistence.EntityNotFoundException;

public interface TransactionService {
     void createTransaction(Authentication authentication,
                            String originAccountNumber,
                            String destinationAccountNumber,
                            Double amount) throws AccessDeniedException, EntityNotFoundException, Exception;


}