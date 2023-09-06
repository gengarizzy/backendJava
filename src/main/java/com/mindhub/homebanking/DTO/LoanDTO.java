package com.mindhub.homebanking.DTO;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public class LoanDTO {

    //Para crear el servicio GET de “/api/loans” debes crear un LoanDTO que puedas usar
    // para retornar los préstamos disponibles, el LoanDTO no necesita tener
    // la propiedad de clientes asociados.

    private long id;

    private String name;

    private double maxAmount;

    private List<Integer> payments;

    public LoanDTO() {
    }

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }



}