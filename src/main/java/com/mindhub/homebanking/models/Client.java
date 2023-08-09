package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native", strategy = "native")
    private long id; //GENERO LA PRIMARY KEY
    private String firstName, lastName, email; //GENERO LOS DATOS DE TIPO STRING


    @OneToMany(mappedBy="client", fetch=FetchType.EAGER) //VINCULACION CON LA TABLA
    Set<Account> accounts = new HashSet<>(); // COLECCION PARA CONTENER LAS CUENTAS QUE PERTENECEN A UN CLIENT

    public Client() { //CONSTRUCT
    }

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    } //UN CLIENTE SE CONSTRUYE CON ESOS 3 PARAMETROS. LA ID ES AUTOMATICA



    //GETTERS AND SETTERS
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Account> getAccounts() {
        return accounts;
    } //GETTER DE LA COLECCION DE ACCOUNTS PERTENECIENTES AL CLIENT

    public void addAccount(Account account) {
        account.setClient(this);
        this.accounts.add(account);
    }
    //SETTER PARA VINCULAR ACCOUNTS A UN CLIENT
}