package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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



    public Account() {
    } //CONSTRUCTOR VACIO
    public Account(String number, LocalDate creationDate, double balance) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;

    } //CONSTRUCTOR CONSIDERANDO EL NUMERO DE CUENTA, FECHA DE CREACION Y EL SALDO



    //SIMILAR A LO QUE HICE CON LOS CLIENTES, VINCULO LA TRANSACCION A LA TABLA DE CUENTAS

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();
    //ESA COLECCION VA A CONTENER TODAS LAS TRANSACCIONES ASIGNADAS



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

    public Client getClient(){
       return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }



    //METODOS DE TRANSACCIONES
    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this); //SIMILAR A LO HECHO EN CLIENT, PARA ASIGNAR LAS TRANSACTIONS A LAS ACCOUNTS
        transactions.add(transaction);
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    //TO STRING


    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                ", client=" + client +
                ", transactions=" + transactions +
                '}';
    }
}