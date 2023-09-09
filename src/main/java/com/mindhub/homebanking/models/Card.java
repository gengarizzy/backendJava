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


    @PrePersist
    public void generateAccountCvvAndNumber() {
        if (cvv == null && number == null) {
            Random random = new Random();

            // Genera un número aleatorio entre 0 y 999 para el CVV
            int randomCvv = random.nextInt(1000);
            cvv = String.format("%03d", randomCvv);

            // Genera un número aleatorio de 16 dígitos para el número de cuenta, hecho
            //con el prefijo 8666 y 3 bloques de 4 numeros aleatorio
            long randomNumber1 = generateRandomNumber(4);
            number = "8666" + " " + String.format("%04d", randomNumber1);
            //Uso el prefijo 8666 para referirme a Mindhub, y le agrego 1 bloques de 4 numeros aleatorios separados
            //por un espacio

        }
    }

    //Genero una funcion para generar numeros POSITIVOS aleatorios, la que uso para generar el numero de tarjeta
    //Hago uso de Math.abs() para que los numeros aleatorios generados sean positivos
    private long generateRandomNumber(int digits) {
        Random random = new Random();
        long min = (long) Math.pow(10, digits - 1);
        long max = (long) Math.pow(10, digits) - 1;
        return Math.abs(min + random.nextLong() % (max - min + 1));
    }

    // Resto de tu clase y métodos
















public Card() {
    }

    public Card(String cardHolder, CardType type, CardColor color,   LocalDate fromDate, LocalDate thruDate) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
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

