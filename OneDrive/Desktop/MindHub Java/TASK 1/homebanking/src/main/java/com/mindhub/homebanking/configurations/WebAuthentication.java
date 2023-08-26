package com.mindhub.homebanking.configurations;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    ClientRepository clientRepository;

    @Bean
    public PasswordEncoder passwordEncoder() { //se implementa el objeto PasswordEncoder
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }




    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(inputEmail-> { //no es inputName, ya que pido el email. Entonces es inputEmail

            Client client = clientRepository.findByEmail(inputEmail);

                    if (client != null) {
       //MODIFICO EL CODIGO PARA ASIGNAR EL VALOR DE AUTHORITY MEDIANTE LA PROPIEDAD roleType de tipo RoleType pasada a String
                        GrantedAuthority authority = new SimpleGrantedAuthority(client.getRoleType().toString());

                        return new User(
                                client.getEmail(),
                                client.getPassword(),
                                Collections.singleton(authority)
                        ); //por defecto doy autoridad como CLIENT

            } else {
            //si no existe el cliente asociado al mail, se da aviso de que no se conoce usuario para ese mail
                throw new UsernameNotFoundException("Unknown user: " + inputEmail);

            }

        }
        );

    }



}



