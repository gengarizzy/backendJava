package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Account;

public class LoanApplicationDTO {

//    Este DTO debe tener id del préstamo, monto, cuotas y número de cuenta de destino.

    //NOTA PARA GENGAR: revisar como se llaman las propiedades en los metodos del Axios antes de nombrarlas en
    //el proyecto. La otra vez paso lo mismo

    private Long loanId; //MOD

    private Double amount;

    private Integer payments;

    private String toAccountNumber; //MOD

    public LoanApplicationDTO(Long loanId, Double amount, Integer payments, String toAccountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
    }

    public Long getId() {
        return loanId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getDestinationAccountNumber() {
        return toAccountNumber;
    }
}
