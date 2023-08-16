package com.mindhub.homebanking.DTO;
import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private String name;
    private int payment;
    private Double amount;

    public ClientLoanDTO(){}

    public ClientLoanDTO(ClientLoan clientLoan){

        id = clientLoan.getId();

        loanId = clientLoan.getLoan().getId();

        name = clientLoan.getLoan().getName();

        amount = clientLoan.getAmount();

        payment = clientLoan.getPayment();

    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Double getAmount() {
        return amount;
    }
    public int getPayment() {
        return payment;
    }

    public Long getLoanId() {
        return loanId;
    }
}