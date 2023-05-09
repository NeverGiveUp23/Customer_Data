package com.felixvargas.customer.repository;

import com.felixvargas.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

// for testing, we only need to test this jpa repo when we add custom query's that are complex, must test
    // the ones below are provided by JPA -> testing isn't necessary


    boolean existsCustomerByEmail(String email);

    Optional<Customer> findCustomerByEmail(String email);

    boolean existsCustomerById(Integer id);


}
