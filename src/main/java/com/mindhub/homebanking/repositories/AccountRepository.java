package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    //Recibe un Account y un dato de tipo Long, que pertenece a su ID


    //Implementar metodo de consulta para buscar cuenta por numero

    Optional<Account> findById(Long id);

    Account findByNumber(String number);

    Account findByClient(Client client);

    Account findByNumberAndClient(String number, Client client);



}
