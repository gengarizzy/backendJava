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
    public void createTransaction(Authentication authentication,
                                  String originAccountNumber,
                                  String destinationAccountNumber,
                                  Double amount)
            throws AccessDeniedException, EntityNotFoundException, Exception{

        Client authorizedClient = clientRepository.findByEmail(authentication.getName());
        Optional<Account> originAccount = accountRepository.findByNumber(originAccountNumber);

        if(originAccount.isEmpty()) throw new EntityNotFoundException("Origin account doesn't exist");

        Optional<Account> destinationAccount = accountRepository.findByNumber(destinationAccountNumber);

        if(destinationAccount.isEmpty()) throw new EntityNotFoundException("The destination account does not exist");

        if(authorizedClient.getId() != originAccount.get().getClient().getId()) throw new AccessDeniedException("The account doesn't belong to the authenticated client");

        if(originAccount.get().getBalance() < amount) throw new Exception("The client has no funds");

        destinationAccount.get().setBalance(destinationAccount.get().getBalance() + amount);

        originAccount.get().setBalance(originAccount.get().getBalance() - amount);

        Transaction transactionDestination = new Transaction(TransactionType.CREDIT,
                amount,
                "Credit from account " + originAccount.get().getNumber(),
                LocalDateTime.now());
        destinationAccount.get().setBalance(destinationAccount.get().getBalance() + amount);

        Transaction transactionOrigin = new Transaction(TransactionType.DEBIT, amount, "Debit to account " +
                destinationAccount.get().getNumber(), LocalDateTime.now());
        originAccount.get().setBalance(originAccount.get().getBalance() - amount);





        destinationAccount.get().addTransaction(transactionDestination);

        originAccount.get().addTransaction(transactionOrigin);




        accountRepository.save(destinationAccount.get());

        accountRepository.save(originAccount.get());

        transactionRepository.save(transactionDestination);

        transactionRepository.save(transactionOrigin);

    }

}
