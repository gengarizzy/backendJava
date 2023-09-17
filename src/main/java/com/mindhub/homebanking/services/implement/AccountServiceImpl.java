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
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
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
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

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


    @Override //SEGUNDO INTENTO
    public void deleteAccount(Authentication authentication, Long id) throws Exception {


        //Pruebo la primera parte del mismo modo que el metodo comentado debajo de este
        Client client = clientService.findByEmail(authentication.getName());

        Account clientAccount = client.getAccounts()
                .stream().filter(account -> account.getId() == id) //obtengo el id con el getter
                .findAny().orElse(null);
        //Asigno la instancia clientAccount a la account que tenga el id deseado


        //verificaciones de nulidad y balance
        if(clientAccount == null){

            throw new EntityNotFoundException("Account not found (SERVICE ERROR)");

        } else if(clientAccount.getBalance() > 0){

            throw new Exception("Your account balance must be zero (SERVICE ERROR)");

        }
        //Ahora en vez de eliminar, puedo intentar cambiar un atributo para permitir o no el rendering
        //Puedo definir un metodo en la clase Account para cambiarle una propiedad
        clientAccount.setEnabled(false);
        //Ahora la cuenta estaria en un estado de DESHABILITADO.
        //Puede que sea una solucion mas eficiente, porque no se eliminaria el historial de la misma
        //Y se podria revertir facilmente en caso de error o necesidad. Deberia buscar la manera de
        //poder hacerlo unicamente con un permiso especial

        accountRepository.save(clientAccount);
        //Guardo la cuenta luego de cambiarle el estado. Ahora depende del rendering

    }
//    @Override
//    public void deleteAccount(Authentication authentication, Long id) throws EntityNotFoundException, Exception {
////REVISA PASO A PASO LO QUE SUCEDE
//
//
//        //PRIMERO BUSCO UN CLIENTE AUTENTICADO Y LO ASIGNO A LA INSTANCIA DE NOMBRE client
//        Client client = clientRepository.findByEmail(authentication.getName());
//
//
//        //Una vez encontrado el client, busco las accounts asociadas al mismo
//        Account clientAccount = client.getAccounts()
//                .stream().filter(account -> account.getId() == id).findAny().orElse(null);
//
//
//
//        //validaciones de account, en caos de que no existan o dispongan de saldo
//        if(clientAccount == null){
//
//            throw new EntityNotFoundException("Accounts not found");
//
//        } else if(clientAccount.getBalance() > 0){
//
//            throw new Exception("You must withdraw your account amount to allow deleting it ");
//
//        }
//
//
//        //Busco las transacciones asociadas a las cuentas, ya que hay que eliminarlas previamente
//        List<Transaction> transactions = accountRepository.findTransactionsById(clientAccount.getId());
//        //Elimino las transactions
//        transactionService.deleteAllByAccount(transactions);
//
//        //DEBERIA HACER LO MISMO PARA LOS LOANS
//
//
//
//
//        accountRepository.delete(clientAccount);
//
//        clientRepository.save(client);
//
//
//
//    }


}
