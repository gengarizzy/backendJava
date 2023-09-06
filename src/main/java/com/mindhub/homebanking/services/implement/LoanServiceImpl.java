package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;



    //Metodo para encontrar prestamo por id
    @Override
    public Loan findLoanById(Long id) {
        return loanRepository.findLoanById(id);
    }


    //Metodo para guardar el prestamo mediante el repository
    @Override
    public void saveNewLoan(Loan loan) {
        loanRepository.save(loan);
    }





    @Override
    public List<LoanDTO> getLoansDTO() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }
    //REPASO DE LA FUNCION DE ARRIBA

    //.findAll() recupera todos los registros de la entidad que el repositorio maneja,

    //.stream() convierte la lista de préstamos recuperados en un flujo (stream) de elementos.
    // Un flujo es una secuencia de elementos que puedes procesar de manera secuencial o paralela.

    //.map() aplica una operación de mapeo a cada elemento del flujo.
    // En este caso, se está utilizando un constructor de la clase LoanDTO para crear un objeto LoanDTO
    // a partir de cada préstamo en el flujo.
    // Básicamente, está transformando los objetos de la entidad Loan en objetos LoanDTO

    //.collect(Collectors.toList()) recopila los elementos mapeados (los LoanDTO) en una lista.
    // El método collect() toma un Collector como argumento, y
    // Collectors.toList() es un Collector que se utiliza para acumular elementos en una lista.


}
