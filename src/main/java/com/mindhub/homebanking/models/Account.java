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
    private LocalDate date;
    private double balance;

    private static int accountCounter = 1; //Inicio mi contador para asignar un numero de cuenta automatico
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;
    //VINCULO UN ACCOUNT A LA TABLA DE CLIENT MEDIANTE JOINCOLUMN Y LA RELACION MUCHOS A UNO.



    public Account() {
    } //CONSTRUCTOR VACIO


    //UPTADE: Ya no le paso mas el numero de cuenta como parametro al constructor
    //ya que quiero generarlo de manera automatica e incremental
    public Account( LocalDate date, double balance) {
        this.date = date;
        this.balance = balance;

    } //CONSTRUCTOR CONSIDERANDO EL NUMERO DE CUENTA, FECHA DE CREACION Y EL SALDO



    //Mediante el metodo generateAccountNumber(), genero un numero de cuenta de manera automatica
    //si number==null (lo que sucede, ya que no le asigno un number mediante un constructor)
    //toma un numero a partir de un contador, el cual se actualiza al crear la cuenta, y utiliza
    //el prefijo VIN-
    @PrePersist
    public void generateAccountNumber() {
        if (number == null) {
            number = "VIN-" + String.format("%06d", accountCounter);
            accountCounter++;
        }
    }



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
    public LocalDate getDate() {
        return date;
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

    public void setNumber(String number) {
        this.number = number;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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
                ", date=" + date +
                ", balance=" + balance +
                ", client=" + client +
                ", transactions=" + transactions +
                '}';
    }
}