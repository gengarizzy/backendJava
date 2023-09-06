package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {

    Loan findLoanById(Long id);

    List<LoanDTO> getLoansDTO ();


    void saveNewLoan(Loan loan);





}
