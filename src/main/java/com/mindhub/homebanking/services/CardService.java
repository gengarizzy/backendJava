package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

public interface CardService {

    ResponseEntity<Object> createCards(@RequestParam CardColor cardColor,
                                       @RequestParam CardType cardType,
                                       Authentication authentication );

    Set<ClientDTO> getClientsDTO();
}
