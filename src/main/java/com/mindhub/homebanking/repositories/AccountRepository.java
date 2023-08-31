package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    //Recibe un Account y un dato de tipo Long, que pertenece a su ID


    //Implementar metodo de consulta para buscar cuenta por numero

    void findByNumber(String number);



}
