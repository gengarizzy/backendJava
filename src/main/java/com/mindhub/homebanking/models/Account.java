package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDate creationDate;
    private double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;
    //VINCULO UN ACCOUNT A LA TABLA DE CLIENT MEDIANTE JOINCOLUMN Y LA RELACION MUCHOS A UNO.

    public Account(String number, LocalDate creationDate, double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;

    }

    public Account() {
    } //



    //GETTERS AND SETTERS
    public long getId() {
        return id;
    }
    public String getNumber() {
        return number;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }
    public double getBalance() {
        return balance;
    }
    public void setClient(Client client) {
        this.client = client;
    }
}