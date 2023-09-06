package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
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
    private CardService cardService;

    @Autowired
    private ClientService clientService;





    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor,
                                              @RequestParam CardType cardType,
                                              Authentication authentication ) {

        //Obtengo el cliente mediante findByEmail del service
        Client client = clientService.findByEmail(authentication.getName());





        //PUNTO MARCADO EN LA CORRECION DE TASK 7. IMPLEMENTO UN IF CON UN BOOLEAN
        //Reviso si el cliente no tiene ya asociada una tarjeta de cierto color y tipo
        //Si la condicion es verdadera, es decir que ya existe una tarjeta con esas condiciones, deniego request
        if (cardService.existsByClientAndColorAndType(client, cardColor, cardType)) {
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
        cardService.saveNewCard(newCard);


        return new ResponseEntity<>("New card created", HttpStatus.CREATED);

    }
}