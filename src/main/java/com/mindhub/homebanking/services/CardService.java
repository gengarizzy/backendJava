package com.mindhub.homebanking.services;
import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public interface CardService {


    void saveNewCard(Card card);


    //SOLUCION: tenia que usar existsBy debido a una convencion en Spring
    boolean existsByClientAndColorAndType(Client client, CardColor cardColor, CardType cardType);


}
