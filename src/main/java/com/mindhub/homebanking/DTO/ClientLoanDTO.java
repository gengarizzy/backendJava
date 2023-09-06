package com.mindhub.homebanking.DTO;
import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private String name;
    private int payments;
    private Double amount;


    public ClientLoanDTO(ClientLoan clientLoan){

        this.id = clientLoan.getId();

        this.loanId = clientLoan.getLoan().getId();

        this.name = clientLoan.getLoan().getName();

        this.amount = clientLoan.getAmount();

        this.payments = clientLoan.getPayments();

    }

    public Long getId() {
        return id;
    }


    public Long getLoanId() {
        return loanId;
    }
    public String getName() {
        return name;
    }
    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }


}