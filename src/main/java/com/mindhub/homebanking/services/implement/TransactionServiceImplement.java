package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImplement implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;
    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void createTransaction(Authentication authentication, String numberOrigin, String numberDestination, Double amount) {



    }
    //El metodo saveTransaction, declarado en TransactionService, recibe una
    //instancia de tipo Transaction, a la que llamamos transaction, y la guardamos mediante el repository
}
