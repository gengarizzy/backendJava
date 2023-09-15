package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {


//Creo un metodo para obtener el cliente, color y tipo de la tarjeta, ya que solo puede tener 1 de cada 1
//    boolean cardExistsByClientAndColorAndType(Client client, CardColor cardColor, CardType cardType);

    //SOLUCION: tenia que usar existsBy debido a una convencion en Spring
    boolean existsByClientAndColorAndType(Client client, CardColor cardColor, CardType cardType);

    List<Card> findCardsByClient(Client client);
}
