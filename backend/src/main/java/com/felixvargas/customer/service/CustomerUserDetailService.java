package com.felixvargas.customer.service;

import com.felixvargas.customer.interfaces.CustomerDAO;
import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerUserDetailService implements UserDetailsService {
    private final CustomerDAO customerDAO;  // <- inject the DAO
    private final CustomerRepository customerRepository;

    public CustomerUserDetailService(@Qualifier("jpa") CustomerDAO customerDAO,
                                     CustomerRepository customerRepository) {
        this.customerDAO = customerDAO;
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerDAO.selectCustomerByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));



    }
}
