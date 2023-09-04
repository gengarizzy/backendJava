package com.mindhub.homebanking.DTO;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import java.time.LocalDateTime;

public class TransactionDTO {




    //VARIABLES
    private Long id;
    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;

    //CONSTRUCTOR
    public TransactionDTO(Transaction transaction){
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.description = transaction.getDescription();
//EL CONSTRUCTOR QUE RECIBE A TRANSACTION BRINDA LOS GETTERS PARA PERMITIR ACCEDER A LA INFO

    }

   //GETTERS. UN DTO SOLO TIENE GETTERS
    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
