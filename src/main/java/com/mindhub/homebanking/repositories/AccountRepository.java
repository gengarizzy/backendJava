package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    //Recibe un Account y un dato de tipo Long, que pertenece a su ID


    //Implementar metodo de consulta para buscar cuenta por numero

    Optional<Account> findById(Long id);

    Account findByNumber(String number);

//    List<Transaction> findTransactionsByAccount(Account account);



    List<Account> findAccountsByClient(Client client);

    Account findByClient(Client client);

    Account findByNumberAndClient(String number, Client client);



}
