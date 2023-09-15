package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Random;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id; //Long Wrapper
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvv;
    private LocalDate fromDate;

    private LocalDate thruDate;


    @ManyToOne(fetch = FetchType.EAGER) //VINCULO A CLIENTES
    @JoinColumn(name = "client_id")
    private Client client;


    @OneToOne(fetch = FetchType.EAGER) //VINCULO A CUENTAS
    @JoinColumn(name = "account_id")
    private Account account;



    // Resto de tu clase y métodos
















public Card() {
    }

    public Card(String cardHolder, CardType type, CardColor color, String number, String cvv,  LocalDate fromDate, LocalDate thruDate) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }

    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


}

