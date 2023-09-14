package com.mindhub.homebanking.controller;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class CardController {


    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;





    @Transactional //Uso Transactional para deshacer la creacion si algo falla
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCards(@RequestParam CardColor cardColor,
                                              @RequestParam CardType cardType,
                                              Authentication authentication ) {
        //VALIDACIONES DE LOS ARGUMENTOS

        if (cardColor == null || cardType == null) {
            return new ResponseEntity<>("CardColor and CardType are required", HttpStatus.BAD_REQUEST);
        }

        if (authentication == null) {
            return new ResponseEntity<>("Authenticated user required", HttpStatus.BAD_REQUEST);

        }

        //Obtengo el cliente mediante findByEmail del service
        Client client = getClientFromAuthentication(authentication);


        //PUNTO MARCADO EN LA CORRECION DE TASK 7. IMPLEMENTO UN IF CON UN BOOLEAN
        //Reviso si el cliente no tiene ya asociada una tarjeta de cierto color y tipo
        //Si la condicion es verdadera, es decir que ya existe una tarjeta con esas condiciones, deniego request
        if (cardService.existsByClientAndColorAndType(client, cardColor, cardType)) {
            return new ResponseEntity<>("Only one type/color per client", HttpStatus.FORBIDDEN);
        }




        String cardHolder = client.getFirstName() + " " + client.getLastName();
        //Uso un metodo de CardService para crear la nueva Card con los parametros provenientes de esta clase
        Card newCard = cardService.createNewCard(cardHolder, cardColor, cardType);

        client.addCard(newCard);
        cardService.saveNewCard(newCard);


        return new ResponseEntity<>("New card created", HttpStatus.CREATED);

    }

    // Función privada para obtener el cliente desde la autenticación
    private Client getClientFromAuthentication(Authentication authentication) {
        String email = authentication.getName();
        return clientService.findByEmail(email);
    }

    //Creo una funcion que me devuelva el nombre completo del cliente autenticado, para facilitar
    //la lectura del codigo de la funcion principal

}