package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Random;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id; //Long Wrapper
    private String cardholder;
    private  CardType type;
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


    @PrePersist
    public void generateAccountCvvAndNumber() {
        if (cvv == null && number == null) {
            Random random = new Random();

            // Genera un número aleatorio entre 0 y 999 para el CVV
            int randomCvv = random.nextInt(1000);
            cvv = String.format("%03d", randomCvv);

            // Genera un número aleatorio de 16 dígitos para el número de cuenta
            long randomNumber = generateRandomNumber(16);
            number = String.format("%016d", randomNumber);
        }
    }

    private long generateRandomNumber(int digits) {
        Random random = new Random();
        long min = (long) Math.pow(10, digits - 1);
        long max = (long) Math.pow(10, digits) - 1;
        return min + random.nextLong() % (max - min + 1);
    }

    // Resto de tu clase y métodos
















public Card() {
    }

    public Card(String cardholder, CardType type, CardColor color,   LocalDate fromDate, LocalDate thruDate) {
        this.cardholder = cardholder;
        this.type = type;
        this.color = color;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }

    public Long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
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

