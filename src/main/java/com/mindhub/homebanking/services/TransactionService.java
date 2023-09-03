package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.security.core.Authentication;

public interface TransactionService {

    Transaction saveTransaction(Transaction transaction);
    //Genera un metodo para guardar la transaccion, el cual va a ser sobreescrito en la implementacion
    //Su funcion es la de utilizar el metodo save del repository

    void createTransaction(Authentication authentication,
                           String numberOrigin,
                           String numberDestination,
                           Double amount);
    //METODO PARA CREAR LA TRANSACCION

}
