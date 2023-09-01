package com.mindhub.homebanking.repositories;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import javax.persistence.EntityNotFoundException;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    Transaction save(Transaction transaction);


}
