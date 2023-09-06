package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.ClientDTO;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;



    @Override
    public void saveNewCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public boolean existsByClientAndColorAndType(Client client, CardColor cardColor, CardType cardType) {
        return cardRepository.existsByClientAndColorAndType(client, cardColor, cardType);
    }


}
