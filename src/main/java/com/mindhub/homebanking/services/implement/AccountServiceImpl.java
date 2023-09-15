package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account createNewDefaultAccount() {
        LocalDate newAccountDate = LocalDate.now();
        Double newAccountBalance = 0.0;
        return new Account(newAccountDate, newAccountBalance);
    }


    @Override
    public Account findByNumberAndClient(String number, Client client) {
        return accountRepository.findByNumberAndClient(number, client);
    }

    @Override
    public ResponseEntity<AccountDTO> getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.map(AccountDTO::new).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Set<AccountDTO>> getClientAccounts(Authentication authentication) {
        Client client = clientRepository.findByEmail(authentication.getName());

        if (client != null) {
            Set<AccountDTO> accountDTO = client.getAccounts()
                    .stream()
                    .map(AccountDTO::new)
                    .collect(Collectors.toSet());
            return ResponseEntity.ok(accountDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }





    //Este metodo recibo una account especifica mediante ID
    @Override
    public AccountDTO getAccountDTO(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(AccountDTO::new)
                .orElse(null);
    }


    //Este metodo recibe todas las accounts
    @Override
    public Set<AccountDTO> getAllAccountsDTO() {
        return accountRepository.findAll()
                .stream()
                .map(AccountDTO::new)
                .collect(Collectors.toSet());
    }


    //TASK 9, necesito un metodo de service para encontrar cuenta por numero
    @Override
    public Account findByNumber(String destinationAccountNumber) {
        return accountRepository.findByNumber(destinationAccountNumber);
    }

    @Override
    public void deleteAccount(Authentication authentication, Long id) throws EntityNotFoundException, Exception {

        Client client = clientRepository.findByEmail(authentication.getName());

        Account clientAccount = client.getAccounts()
                .stream().filter(account -> account.getId() == id).findAny().orElse(null);



        if(clientAccount == null){

            throw new EntityNotFoundException("Account not found");

        } else if(clientAccount.getBalance() > 0){

            throw new Exception("You must withdraw your account amount to allow deleting it ");

        }


        // Obtener todas las transacciones del cliente
//        List<Transaction> clientTransactions = accountRepository.findTransactionsByAccount(clientAccount);

//        // Eliminar todas las transacciones del cliente
//        for (Transaction transaction : clientTransactions) {
//            transactionRepository.delete(transaction);
//        }




        // Obtener todas las tarjetas del cliente
        List<Card> clientCards = cardRepository.findCardsByClient(client);

        // Eliminar todas las transacciones del cliente
        for (Card card : clientCards) {
            cardRepository.delete(card);
        }







        accountRepository.delete(clientAccount);

        clientRepository.save(client);



    }


}
