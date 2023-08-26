package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;



    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor,
                                              @RequestParam CardType cardType,
                                              Authentication authentication ) {


        Client client = clientRepository.findByEmail(authentication.getName());

        //Reviso si el cliente no tiene ya asociada una tarjeta de cierto color y tipo
        if (cardRepository.findByClientAndColorAndType(client, cardColor, cardType) != null) {
            return new ResponseEntity<>("Only one type/color per client", HttpStatus.FORBIDDEN);
        }

        String cardHolder = client.getFirstName() + " " + client.getLastName();


        Card cardNew = new Card(cardHolder,
                cardType,
                cardColor,
                LocalDate.now(),
                LocalDate.now().plusYears(5));

        client.addCard(cardNew);
        cardRepository.save(cardNew);


        return new ResponseEntity<>("New card created", HttpStatus.CREATED);


    }
}
