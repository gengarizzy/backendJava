package com.mindhub.homebanking.DTO;
import com.mindhub.homebanking.models.Account;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;


public class AccountDTO {
    private long id;
    private String number;
    private LocalDate date;
    private double balance;

    private Set<TransactionDTO> transactions;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.date = account.getDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());
    }


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
    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
}