package com.mindhub.homebanking;
import java.time.LocalDate;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");//genero user
			Account account1 = new Account("VIN001",LocalDate.now(), 5000.0);//cuenta1
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500.0);//cuenta2
			//NO ENTIENDO EL ERROR AL GENERAR LAS ACCOUNTS
			//NO ME MUESTRA EL TIPO DE DATO DE FECHA AL INTRODUCIRLO, Y LAS CARDS NO ME LA RECONOCEN

			clientRepository.save(client1); //GUARDO EL CLIENT GENERADO
			client1.addAccount(account1); //ASOCIO LAS CUENTAS AL CLIENTE
			client1.addAccount(account2);
			accountRepository.save(account1); //GUARDO LAS CUENTAS YA ASIGNADAS AL CLIENT
			accountRepository.save(account2);
		};
	}

}
