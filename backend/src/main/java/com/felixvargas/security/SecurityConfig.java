package com.felixvargas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager( // gets a list of providers
            AuthenticationConfiguration configuration // <- inject the configuration
    ) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean // <- create a bean of type AuthenticationProvider
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService, PasswordEncoder passwordEncoder // <- inject the user details service and the password encoder
    ){
         DaoAuthenticationProvider daoAuthenticationProvider =
                new DaoAuthenticationProvider();

         daoAuthenticationProvider.setPasswordEncoder(passwordEncoder); // <- set the password encoder
         daoAuthenticationProvider.setUserDetailsService(userDetailsService); // <- set the user details service

    return daoAuthenticationProvider;
    }


}
