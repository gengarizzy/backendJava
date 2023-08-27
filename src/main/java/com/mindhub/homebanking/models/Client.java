package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "native", strategy = "native")
    private long id; //GENERO LA PRIMARY KEY
    private String firstName;

    private String lastName;

    private String email;

    private String password;


    private RoleType roleType;


    @OneToMany(mappedBy="client", fetch=FetchType.EAGER) //VINCULACION CON LA TABLA
    Set<Account> accounts = new HashSet<>(); // COLECCION PARA CONTENER LAS CUENTAS QUE PERTENECEN A UN CLIENT

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER) //VINCULACION CON LA TABLA
    private Set<ClientLoan> clientLoans = new HashSet<>(); // COLECCION PARA CONTENER LOS PRESTAMOS QUE PERTENECEN A UN CLIENT


    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER) //VINCULACION CON LA TABLA
    private Set<Card> cards = new HashSet<>(); //COLECCION PARA CONTENER LAS TARJETAS QUE PERTENECEN A UN CLIENT


    public Client() { //CONSTRUCT VACIO
    }


  //CONSTRUCT CON LOS PARAMETROS
    public Client(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    } //UN CLIENTE SE CONSTRUYE CON ESOS 3 PARAMETROS. LA ID ES AUTOMATICA



    //GETTERS AND SETTERS
    public long getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Account> getAccounts() {
        return accounts;
    } //GETTER DE LA COLECCION DE ACCOUNTS PERTENECIENTES AL CLIENT

    public void addAccount(Account account) { //METODO PARA AGREGAR LAS CUENTAS AL OBJETO CLIENTE
        account.setClient(this);
        this.accounts.add(account);
    }
    //SETTER PARA VINCULAR ACCOUNTS A UN CLIENT

    public Set<Loan> getLoans(){
        return clientLoans.stream().map(clientLoan -> clientLoan.getLoan()).collect(Collectors.toSet());
    }


    //Genero una coleccion para gestionar los prestamos al cliente
    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    //Genero un metodo para agregar prestamos al Set ClientLoan del cliente
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        this.clientLoans.add(clientLoan);
    }


    public Set<Card> getCards() { //GETTERS Y SETTERS PARA LAS TARJETAS DEL CLIENTE
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card){ //SETTER PARA AGREGAR TARJETAS AL CLIENTE
        card.setClient(this);
        cards.add(card);
    }

    public RoleType getRoleType() { //GETTER PARA EL ROL DEL USUARIO
        return roleType;
    }

    public void setRoleType(RoleType roleType) { //SETTER PARA EL ROL DEL USUARIO
        roleType = roleType;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", roleType=" + roleType +
                ", accounts=" + accounts +
                ", clientLoans=" + clientLoans +
                ", cards=" + cards +
                '}';
    }
}