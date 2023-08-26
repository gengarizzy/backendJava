package com.mindhub.homebanking;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HomebankingApplication {



	//Genero la propiedad PasswordEncoder para cifrar las password
	//Ademas tengo que emplearla para generar las password a los clients
	@Autowired
	PasswordEncoder passwordEncoder;

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
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba"),RoleType.CLIENT);//genero user1
			Client client2 = new Client("Client2", "Client2", "client2@mindhub.com", passwordEncoder.encode("password"),RoleType.CLIENT);//genero user2
			Client admin = new Client("admin","admin", "admin@admin.com", passwordEncoder.encode("admin"),RoleType.ADMIN);

			//CREO CUENTAS
			Account account1 = new Account("VIN001",LocalDate.now(), 5000.0);//cuenta1
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);//cuenta2
			Account account3 = new Account("VIN003", LocalDate.now(), 15000);//cuenta3
			Account account4 = new Account("ADMIN", LocalDate.now(), 1500000);//cuenta admin

//CREO TRANSACCIONES
			Transaction transaction1  = new Transaction(TransactionType.CREDIT, 2000.0, "deposit", LocalDateTime.now());
			Transaction transaction2  = new Transaction(TransactionType.CREDIT, 1000.0, "deposit", LocalDateTime.now());
			Transaction transaction3  = new Transaction(TransactionType.DEBIT, 500.0, "extraction", LocalDateTime.now());
			Transaction transaction4  = new Transaction(TransactionType.DEBIT, 300.0, "extraction", LocalDateTime.now());
			Transaction transaction5  = new Transaction(TransactionType.CREDIT, 800.0, "deposit", LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, 950.0, "extraction", LocalDateTime.now());
			Transaction transaction7 = new Transaction(TransactionType.DEBIT, 9500.0, "extraction", LocalDateTime.now());
			Transaction transaction8 = new Transaction(TransactionType.DEBIT, 9500.0, "deposit", LocalDateTime.now());



			Loan loanMortgage = new Loan(LoanType.MORTGAGE.name(), 500000.0, List.of(12,24,36,48,60)); //Hasta 60 cuotas
			Loan loanPersonal1 = new Loan(LoanType.PERSONAL.name(), 50000.0, List.of(6,12,24)); //24 cuotas
			Loan loanPersonal2 = new Loan(LoanType.PERSONAL.name(), 100000.0, List.of(6,12,24)); //24 cuotas
			Loan loanAutomotive = new Loan(LoanType.AUTOMOTIVE.name(), 300000.0, List.of(6,12,24,36)); //36 cuotas




			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);
			account4.addTransaction(transaction7);
			account4.addTransaction(transaction8);


			client1.addAccount(account1); //ASOCIO LAS CUENTAS AL CLIENTE
			client1.addAccount(account2);
			client2.addAccount(account3);
			admin.addAccount(account4);


			//GENERO LAS ENTIDADES DE TIPO CLIENTLOAN
			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60);
			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12);
			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24);
			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36);
			ClientLoan clientLoan5 = new ClientLoan(200000.0, 36);
			ClientLoan clientLoan6 = new ClientLoan(200000.0, 36);


			//MODIFICACION

			//Cambie el constructor y agregue metodos addClient y addLoan para asignar cliente y prestamo
			//fuera del constructor, como se me indico en la correccion de la task5
			clientLoan1.addClient(client1);
			clientLoan1.addLoan(loanMortgage);

			clientLoan2.addClient(client1);
			clientLoan2.addLoan(loanPersonal1);

			clientLoan3.addClient(client2);
			clientLoan3.addLoan(loanPersonal2);

			clientLoan4.addClient(client2);
			clientLoan4.addLoan(loanAutomotive);

			clientLoan5.addClient(admin);
			clientLoan5.addLoan(loanAutomotive);

			clientLoan6.addClient(admin);
			clientLoan6.addLoan(loanAutomotive);



			//RELACIONO
			loanMortgage.addClientLoan(clientLoan1);
			loanPersonal1.addClientLoan(clientLoan2);
			loanPersonal2.addClientLoan(clientLoan3);
			loanAutomotive.addClientLoan(clientLoan4);
			loanAutomotive.addClientLoan(clientLoan5);
			loanAutomotive.addClientLoan(clientLoan6);

			//Vinculo cada prestamo a un cliente mediante el metodo addClientLoan de la clase Client
			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);
			admin.addClientLoan(clientLoan5);
			admin.addClientLoan(clientLoan6);




			clientRepository.save(client1); //GUARDO LOS CLIENT
			clientRepository.save(client2);
			clientRepository.save(admin);


			accountRepository.save(account1); //GUARDO LAS CUENTAS YA ASIGNADAS AL CLIENT
			accountRepository.save(account2);
			accountRepository.save(account3);
			accountRepository.save(account4);


			transactionRepository.save(transaction1); //GUARDO LAS TRANSACCIONES EN EL TRANSACTION REPOSITORY
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);

			loanRepository.save(loanMortgage);
			loanRepository.save(loanPersonal1);
			loanRepository.save(loanPersonal2);
			loanRepository.save(loanAutomotive);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);
			clientLoanRepository.save(clientLoan5);
			clientLoanRepository.save(clientLoan6);




			Card cardGold = new Card(client1.getFirstName() +" " + client1.getLastName(),
					CardType.DEBIT, CardColor.GOLD, "123456789012345", 123,
					LocalDate.now(), LocalDate.now().plusYears(5));

			Card cardTitanium = new Card(client1.getFirstName() +" " + client1.getLastName(),
					CardType.CREDIT, CardColor.TITANIUM, "1234512345123450", 321,
					LocalDate.now(), LocalDate.now().plusYears(5));

			Card cardSilver = new Card(client2.getFirstName() +" " + client2.getLastName(),
					CardType.CREDIT, CardColor.SILVER, "1234512345000000", 111,
					LocalDate.now(), LocalDate.now().plusYears(2));

			Card cardSilverAdmin = new Card(admin.getFirstName() +" " + admin.getLastName(),
					CardType.CREDIT, CardColor.SILVER, "123ADMIN456", 111,
					LocalDate.now(), LocalDate.now().plusYears(2));



			client1.addCard(cardGold);
			client1.addCard(cardTitanium);
			client2.addCard(cardSilver);
			admin.addCard(cardSilverAdmin);



			cardRepository.save(cardGold);
			cardRepository.save(cardTitanium);
			cardRepository.save(cardSilver);
			cardRepository.save(cardSilverAdmin);



		};
	}

}