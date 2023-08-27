package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {


    //GENERO LAS VARIABLES DE TRANSACTION
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id") //VINCULO TRANSACCION CON CUENTA
    private Account account;

    //CONSTRUCTORES

    public Transaction(){} //CONSTRUCTOR VACIO
    public Transaction(TransactionType type, Double amount, String description, LocalDateTime date){
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }//CONSTRUCTOR CONSIDERANDO TIPO, MONTO, DESCRIPCION Y FECHA



    //GETTERS Y SETTERS
    public Long getId() {
        return id;
    }


    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    //METHOD TO STRING


    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", account=" + account +
                '}';
    }
}
