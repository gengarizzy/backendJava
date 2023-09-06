package com.mindhub.homebanking.configurations;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()//Defino los endpoints autorizados para cada rol

                //ADMIN PUEDE ENTRAR A /ADMIN y REST



                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/rest/**").hasAuthority("ADMIN")


                //CLIENT PUEDE ENTRAR A LO DEMAS
                .antMatchers("/web/accounts.html").hasAnyAuthority("CLIENT", "ADMIN") //doy permiso a estos endpoints
                .antMatchers("/web/account.html").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/web/cards.html").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers("/web/cards.html").hasAnyAuthority("CLIENT", "ADMIN")
                .antMatchers(HttpMethod.GET,"/api/loans").hasAnyAuthority("CLIENT", "ADMIN")




                .antMatchers(HttpMethod.GET, "/api/clients/current/**").hasAnyAuthority("CLIENT", "ADMIN")

                //TENGO UN PROBLEMA ACA. EL METODO getData de transfers.js falla al solicitar los datos del usuario
                //axios.get("/api/clients/current/accounts")
//
//              .antMatchers(HttpMethod.GET,"/api/clients/current/accounts").hasAnyAuthority("CLIENT", "ADMIN")

                .antMatchers(HttpMethod.POST,"/api/transactions").hasAnyAuthority("CLIENT","ADMIN")




                .antMatchers(HttpMethod.POST,"/api/login").permitAll() //No se necesita permiso para intentar loggearse





                //Agrego codigo para que, en caso de no estar autenticado, redirigir al login
                .and()
                .exceptionHandling()
                .accessDeniedHandler((req, res, exc) -> {
                    if (!req.isUserInRole("CLIENT") && !req.isUserInRole("ADMIN")) {
                        res.sendRedirect("/web/index.html"); // Redirigir si no hay permisos ni autenticación
                    } else {
                        res.sendRedirect("/web/accessDenied.html"); // Redirigir si hay autenticación pero no permisos
                    }
                });


        http.formLogin() //doy los parametros pedidos

                .usernameParameter("email")

                .passwordParameter("password")

                .loginPage("/api/login"); //defino el endpoint para loggearse


        http.logout().logoutUrl("/api/logout").deleteCookies();
        //defino el endpoint para cerrar sesion y le indico que elimine las cookies para evitar que alguien mas entre luego


        http.csrf().disable(); //desactivo la verificacion csrf

        http.headers().frameOptions().disable(); //NO ME FUNCIONA COMO httpSecurity. POR QUE? Los demas tambien son http solo

        // if user is not authenticated, just send an authentication failure response

        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication

        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response

        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response

        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }






}