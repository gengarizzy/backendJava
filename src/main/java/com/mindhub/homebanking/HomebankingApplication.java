package com.mindhub.homebanking;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	//Utilizo la interfaz CommandLineRunner para ejecutar este codigo al iniciar la app

	//En el CommandLineRunner debes crear 3 préstamos y guardarlos en la base de datos:
	//Mantengo los nombres en ingles
	//Hipotecario: monto máximo 500.000, cuotas 12,24,36,48,60. --> mortgage
	//Personal: monto máximo 100.000, cuotas 6,12,24 --> personal
	//Automotriz: monto máximo 300.000, cuotas 6,12,24,36 --> automotive

	//Tengo que generar 3 clients y 3 accounts al menos para asignar un tipo de prestamo a cadar uno
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			//REORGANIZO PARA SEGUIR UN FLUJO DE CREACION, ASIGNACION Y GUARDADO
			//CREACION----------------------------------------------------------------

			//CREO CLIENTE
			Client client1 = new Client("Melba", "Morel", "melba1@mindhub.com");//genero user1
			Client client2 = new Client("Client2", "Client2", "melba2@mindhub.com");//genero user2
//			Client client3 = new Client("Client3", "Client3", "melba3@mindhub.com");//genero user3



			//CREO CUENTAS
			Account account1 = new Account("VIN001",LocalDate.now(), 5000.0);//cuenta1
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);//cuenta2
			Account account3 = new Account("VIN003", LocalDate.now(), 15000);//cuenta3



			//CREO TRANSACCIONES
			Transaction transaction1  = new Transaction(TransactionType.CREDIT, 2000.0, "deposit", LocalDateTime.now());
			Transaction transaction2  = new Transaction(TransactionType.CREDIT, 1000.0, "deposit", LocalDateTime.now());
			Transaction transaction3  = new Transaction(TransactionType.DEBIT, 500.0, "extraction", LocalDateTime.now());
			Transaction transaction4  = new Transaction(TransactionType.DEBIT, 300.0, "extraction", LocalDateTime.now());
			Transaction transaction5  = new Transaction(TransactionType.CREDIT, 800.0, "deposit", LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, 950.0, "extraction", LocalDateTime.now());

			//CREO LOS ENTIDADES DE CLASE LOAN PARA LOS PRESTAMOS
			//(String name, Double maxAmount, List<Integer> payments) lleva ese constructor
			//EMPLEO LO APRENDIDO DE ENUMS PARA INDICAR EL TIPO DE CREDITO
			Loan loanMortgage = new Loan(LoanType.MORTGAGE.name(), 500000.0, List.of(12,24,36,48,60)); //Hasta 60 cuotas
			Loan loanPersonal1 = new Loan(LoanType.PERSONAL.name(), 50000.0, List.of(6,12,24)); //24 cuotas
			Loan loanPersonal2 = new Loan(LoanType.PERSONAL.name(), 100000.0, List.of(6,12,24)); //24 cuotas
			Loan loanAutomotive = new Loan(LoanType.AUTOMOTIVE.name(), 300000.0, List.of(6,12,24,36)); //36 cuotas

			// Paso el codigo aca porque no me funciona al final. REVISAR LUEGO!!!




			//ASIGNACION----------------------------------------------------------------
			//Asigno transacciones a las cuentas, y las cuentas al cliente

			//TRANSACCIONES A CUENTAS
			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			//LUEGO LO HAGO TODO DE CERO Y ORDENADO
			//RECORDAR QUE ESTOS CLIENT, ACCOUNTS, ETC, SOLO SE GENERAN POR PRUEBA. LUEGO SE VAN A GENERAR DESDE EL FRONT


			//CUENTAS A CLIENTES
			client1.addAccount(account1); //ASOCIO LAS CUENTAS AL CLIENTE
			client1.addAccount(account2);
			client2.addAccount(account3);



			//GENERO LAS ENTIDADES DE TIPO CLIENTLOAN
			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60, client1, loanMortgage);
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12, client1, loanPersonal1);
			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24, client2, loanPersonal2);
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36, client2, loanAutomotive);

			//RELACIONO
			loanMortgage.addClientLoan(clientLoan1);
			loanPersonal1.addClientLoan(clientLoan2);
			loanPersonal2.addClientLoan(clientLoan3);
			loanAutomotive.addClientLoan(clientLoan4);



			//Vinculo cada prestamo a un cliente mediante el metodo addClientLoan de la clase Client
			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
//			client2.addClientLoan(clientLoan2);
//			client3.addClientLoan(clientLoan3);








			//UNA VEZ CREADAS LAS CUENTAS, CLIENTES Y TRASACCIONES, Y POSTERIORMENTE ASIGNADAS, USO REPOSITORY PARA GUARDAR

			clientRepository.save(client1); //GUARDO LOS CLIENT
			clientRepository.save(client2);
//			clientRepository.save(client3);

			accountRepository.save(account1); //GUARDO LAS CUENTAS YA ASIGNADAS AL CLIENT
			accountRepository.save(account2);
			accountRepository.save(account3);



			transactionRepository.save(transaction1); //GUARDO LAS TRANSACCIONES EN EL TRANSACTION REPOSITORY
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			//No era eso. No me esta generando 3 client, sino 1. SEGUIR REVISANDO!!!
			// LAS GUARDO ANTES DE ASIGNARLAS A LAS CUENTAS
			loanRepository.save(loanMortgage);
			loanRepository.save(loanPersonal1);
			loanRepository.save(loanPersonal2);
			loanRepository.save(loanAutomotive);


			//GUARDO LOS PRESTAMOS EN EL REPOSITORY QUE EXTIENDE JPA
			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);




			//tarjetas

			Card cardGold = new Card(client1.getFirstName() +" " + client1.getLastName(),
					CardType.DEBIT, CardColor.GOLD, "123456789012345", 123,
					LocalDate.now(), LocalDate.now().plusYears(5));

			Card cardTitanium = new Card(client1.getFirstName() +" " + client1.getLastName(),
					CardType.CREDIT, CardColor.TITANIUM, "1234512345123450", 321,
					LocalDate.now(), LocalDate.now().plusYears(5));

			Card cardSilver = new Card(client2.getFirstName() +" " + client2.getLastName(),
					CardType.CREDIT, CardColor.SILVER, "1234512345000000", 111,
					LocalDate.now(), LocalDate.now().plusYears(2));


			client1.addCard(cardGold);
			client1.addCard(cardTitanium);
			client2.addCard(cardSilver);

			cardRepository.save(cardGold);
			cardRepository.save(cardTitanium);
			cardRepository.save(cardSilver);













		};
	}

}