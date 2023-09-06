package com.mindhub.homebanking.controller;

import com.mindhub.homebanking.DTO.LoanApplicationDTO;
import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientLoanService clientLoanService;






    //Para crear el servicio GET de “/api/loans” debes crear un LoanDTO que puedas
    // usar para retornar los préstamos disponibles,
    // el LoanDTO no necesita tener la propiedad de clientes asociados.
    @RequestMapping("/loans")
    public List<LoanDTO> getAvailableLoans () {
        return loanService.getLoansDTO();
    }


    //Para el nuevo recurso a crear se debe tomar en cuenta:
    //
    //Debe recibir un objeto de solicitud de crédito con los datos del préstamo

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> approveNewLoan(
            Authentication authentication,
            @RequestBody LoanApplicationDTO newLoanRequest){


        // LoanApplicationDTO: Este DTO debe tener id del préstamo, monto, cuotas y número de cuenta de destino.
        //Verificar que los datos sean correctos, es decir no estén vacíos.
        //Recibe un cliente autenticado y solicita mediante @RequestBody las propiedades especificadas

        //verifico cliente autenticado
        Client client = clientService.findByEmail(authentication.getName());


        //Verifico los datos de LoanApplicationDTO
        if(newLoanRequest.getId() == null){
            return new ResponseEntity<>("Missing ID", HttpStatus.FORBIDDEN);
        }

        if(newLoanRequest.getAmount() == null){
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }

        //Operator '==' cannot be applied to 'int', 'null'
        //tuve que cambiar el atributo payments a Integer, ya que no me permite int == null
        if(newLoanRequest.getPayments() == null){ //Operator '==' cannot be applied to 'int', 'null'
            return new ResponseEntity<>("Missing destination account number", HttpStatus.FORBIDDEN);
        }


        //Verificar que la cuenta de destino exista
        if(newLoanRequest.getDestinationAccountNumber() == null){
            return new ResponseEntity<>("Missing destination account number", HttpStatus.FORBIDDEN);
        }

        //Una vez que pasan las validaciones del @RequestBody, es decir
        //que tiene todas las propiedades declaradas en LoanApplicationDTO, sigue con la logica





        //Un prestamo va asociado a una cuenta, por lo que necesito vincular una
        Account account = accountService.findByNumber(newLoanRequest.getDestinationAccountNumber());

        double LoanMaximumAmountAllowed = loanService.findLoanById
                (newLoanRequest.getId()).getMaxAmount();
        //Primero busco el loan mediante id y luego el getter del max amount del Loan Model



        //Verificar que el préstamo exista
        Loan loan = loanService.findLoanById(newLoanRequest.getId());
        if(loan == null){
            return new ResponseEntity<>("Loan doesn't exists", HttpStatus.FORBIDDEN);
        }

        //Verificar que el monto solicitado no exceda el monto máximo del préstamo
        if (newLoanRequest.getAmount()>LoanMaximumAmountAllowed){
            return new ResponseEntity<>("The required amount is higher than the maximum allowed amount", HttpStatus.FORBIDDEN);
        }


        //Verifica que la cantidad de cuotas se encuentre entre las disponibles del préstamo
        if (!loan.getPayments().contains(newLoanRequest.getPayments())){
            return new ResponseEntity<>("Quota (payments) number not allowed", HttpStatus.FORBIDDEN);
        }

        //Verificar que la cuenta de destino pertenezca al cliente autenticado
        if(!client.getAccounts().contains(account)){
            return new ResponseEntity<>("This account is not from the authorized client", HttpStatus.FORBIDDEN);
        }

        //UNA VEZ PASADAS TODAS LAS VERIFICACIONES, SE PROCEDE A EMITIR EL PRESTAMO DE ACUERDO A LOS REQUERIMIENTOS

        //Se debe crear una solicitud de préstamo con el monto solicitado sumando el 20% del mismo
        ClientLoan newClientLoan = new ClientLoan(
                newLoanRequest.getAmount() + newLoanRequest.getAmount()*(0.2),
                newLoanRequest.getPayments());
        //MONTO VERIFICADO! Probe con 50, y al cliente se le carga un debe de 60.

        //Se debe crear una transacción “CREDIT” asociada a la cuenta de destino
        // (el monto debe quedar positivo) con la descripción concatenando el nombre del préstamo y la frase “loan approved”
        Transaction newTransaction = new Transaction(
                TransactionType.CREDIT,
                newLoanRequest.getAmount(),
                loan.getName() + " loan approved",
                LocalDateTime.now());


        //Se debe actualizar la cuenta de destino sumando el monto solicitado.
        //Es decir, le asigno su mismo saldo y le agrego el solicitado como prestamo
        account.setBalance(account.getBalance() + newLoanRequest.getAmount());

        //Tambien necesito vincular la transaccion a la cuenta
        account.addTransaction(newTransaction);

        //Esta cuenta pertenece a un cliente, por lo que hay que utilizar tambien el ClientLoan
        client.addClientLoan(newClientLoan);
        //Tambien hay que guardar el prestamo
        loan.addClientLoan(newClientLoan);

        //Una vez creado y vinculado T0DO lo necesario, solo queda utilizar los servicios para guardar en la base de datos

        //CREADOS: clientLoan, transaction, loan

        clientLoanService.saveNewClientLoan(newClientLoan);

        transactionService.saveNewTransaction(newTransaction);

        loanService.saveNewLoan(loan);

        //ACTUALIZADOS: account, client. Deben guardarse los cambios

        accountService.saveAccount(account);

        clientService.saveClient(client);


        //Para finalizar, el metodo requiere una ResponseEntity

        return new ResponseEntity<>("Congratulations. Your loan has been approved",HttpStatus.CREATED);





    }
}
