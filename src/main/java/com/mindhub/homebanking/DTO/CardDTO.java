package com.mindhub.homebanking.DTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;

public class CardDTO {

    private Long id; //Long wrapper
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvv;

    private Client client;
    private LocalDate fromDate;

    private LocalDate thruDate;


    public CardDTO(Card card) { //CONSTRUCTOR
        this.id = card.getId();
        this.cardHolder = card.getCardHolder();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.client = card.getClient();

    }

    //GETTERS, EN DTO SOLO VAN GETTERS


    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }



    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
}
