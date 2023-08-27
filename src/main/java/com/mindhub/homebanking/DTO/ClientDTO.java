package com.mindhub.homebanking.DTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.RoleType;

import java.util.Set;
import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private RoleType roleType;

    private Set<AccountDTO> accounts; //ATENCION. ACA TENIA UN ERROR EN EL CLIENTDTO, QUE NO MOSTRABA CUENTAS, PRESTAMOS Y TARJETAS
    private Set<ClientLoanDTO> loans;
    private Set<CardDTO> cards;


    public ClientDTO(Client client) { //CONSTRUCTOR. ACTUALIZAR AL AGREGAR FUNCIONES
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(toSet());
        this.loans = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(toSet());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(toSet());
        this.roleType = client.getRoleType(); //Agrego role

    }


    //GETTERS
    public long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public RoleType getRoleType() { return roleType; }




    //GETTERS DE LAS FUNCIONES COMO CUENTAS, TARJETAS, ETC
    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}