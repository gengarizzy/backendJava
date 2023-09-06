package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;





    @Override
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor,
                                              @RequestParam CardType cardType,
                                              Authentication authentication ) {

        //Obtengo el cliente mediante findByEmail del repository
        Client client = clientRepository.findByEmail(authentication.getName());


        //Reviso si el cliente no tiene ya asociada una tarjeta de cierto color y tipo
        if (cardRepository.findByClientAndColorAndType(client, cardColor, cardType) != null) {
            return new ResponseEntity<>("Only one type/color per client", HttpStatus.FORBIDDEN);
        }


        String cardHolder = client.getFirstName() + " " + client.getLastName();
        //--PROBLEMA-- Quiero obtener nombre y apellido del client, pero no estoy pudiendo
        Card newCard = new Card(
                cardHolder,
                cardType,
                cardColor,
                LocalDate.now(),
                LocalDate.now().plusYears(5));


        client.addCard(newCard);
        cardRepository.save(newCard);


        return new ResponseEntity<>("New card created", HttpStatus.CREATED);


    }


}
