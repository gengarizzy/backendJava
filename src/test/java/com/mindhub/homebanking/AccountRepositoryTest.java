package com.mindhub.homebanking;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountRepositoryTest {


    @Autowired
    AccountRepository accountRepository;

    @Test
    public void existAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void accountsWithPositiveBalance() {
        List<Account> accounts = accountRepository.findAll();

        // Usando Java Streams para verificar que todos los balances sean mayores o iguales a 0
        boolean allBalancesPositive = accounts.stream()
                .allMatch(account -> account.getBalance() >= 0);

        //Mensaje de error si el saldo es menor a 0
        assertThat("All account balances should be positive or zero", allBalancesPositive);
    }
}





