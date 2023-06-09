package com.felixvargas.customer.service;


import com.felixvargas.customer.interfaces.CustomerDAO;
import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.enums.Gender;
import com.felixvargas.customer.repository.CustomerRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDAO {

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer felix = new Customer(
                1,
                "Felix",
                "Felix@gmail.com",
                "password",
                29,
                Gender.MALE);
        customers.add(felix);

        Customer jane = new Customer(
                2,
                "Jane",
                "Jane@gmail.com",
                "password",
                45,
                Gender.MALE);
        customers.add(jane);
    }

    private final CustomerRepository customerRepository;

    public CustomerListDataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // returns a list of all customers
    @Override
    public List<Customer> selectAllCustomer() {
        return customers;
    }

    // Optional check if customer exists based off of their id
    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    // Delete user by id
    @Override
    public void deleteCustomerById(Integer customerId) {
        customers.stream().filter(c -> c.getId().equals(customerId)).findFirst().ifPresent(customers::remove);
    }

    // find user by id
    @Override
    public boolean existsPersonWithId(Integer id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    // Update customer
    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }

    @Override
    public void updateCustomerProfileImageId(String profileImageId, Integer customerId) {
        // TODO: implement this
    }

    // insert customer into the List
    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    // Checking if customer exists via email
    @Override
    public boolean existsPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public Optional<Customer> selectCustomerByEmail(String email) {
        return customers.stream()
                .filter(c -> c.getUsername().equals(email))
                .findFirst();
    }
}
