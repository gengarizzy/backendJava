package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    //Recibe un Account y un dato de tipo Long, que pertenece a su ID

//    Antes de continuar debes crear un método de consulta en el repositorio de cuentas para buscar una cuenta por su
//        número, de manera que más adelante lo usemos en el controlador. Recuerda cómo hiciste el método de buscar un
//    cliente por su email en el repositorio de clientes.

    Account findByNumber(String number);

    Account findByClient(Client client);





}
