package com.mindhub.homebanking;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
		return (args) -> {

			//REORGANIZO PARA SEGUIR UN FLUJO DE CREACION, ASIGNACION Y GUARDADO
			//CREACION----------------------------------------------------------------

			//CREO CLIENTE
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");//genero user

			//CREO CUENTAS
			Account account1 = new Account("VIN001",LocalDate.now(), 5000.0);//cuenta1
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);//cuenta2

			//CREO TRANSACCIONES
			Transaction transaction1  = new Transaction(TransactionType.CREDIT, 2000.0, "deposit", LocalDateTime.now());
			Transaction transaction2  = new Transaction(TransactionType.CREDIT, 1000.0, "deposit", LocalDateTime.now());
			Transaction transaction3  = new Transaction(TransactionType.DEBIT, 500.0, "extraction", LocalDateTime.now());
			Transaction transaction4  = new Transaction(TransactionType.DEBIT, 300.0, "extraction", LocalDateTime.now());


			//ASIGNACION----------------------------------------------------------------
			//Asigno transacciones a las cuentas, y las cuentas al cliente

			//TRANSACCIONES A CUENTAS
			account1.addTransaction(transaction1);
			account2.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account2.addTransaction(transaction4);

			//CUENTAS A CLIENTES
			client1.addAccount(account1); //ASOCIO LAS CUENTAS AL CLIENTE
			client1.addAccount(account2);

			//UNA VEZ CREADAS LAS CUENTAS, CLIENTES Y TRASACCIONES, Y POSTERIORMENTE ASIGNADAS, USO REPOSITORY PARA GUARDAR
			clientRepository.save(client1); //GUARDO EL CLIENT GENERADO

			accountRepository.save(account1); //GUARDO LAS CUENTAS YA ASIGNADAS AL CLIENT
			accountRepository.save(account2);

			transactionRepository.save(transaction1); //GUARDO LAS TRANSACCIONES EN EL TRANSACTION REPOSITORY
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);














		};
	}

}
