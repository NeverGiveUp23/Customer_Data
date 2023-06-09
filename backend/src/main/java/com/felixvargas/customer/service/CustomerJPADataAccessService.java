package com.felixvargas.customer.service;

import com.felixvargas.customer.interfaces.CustomerDAO;
import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJPADataAccessService implements CustomerDAO {

    // inject Repository
    private final CustomerRepository customerRepository;

    // Use constructor injection
    public CustomerJPADataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // getting findAll() -> customerRepository
    @Override
    public List<Customer> selectAllCustomer() {
        Page<Customer> page = customerRepository.findAll(Pageable.ofSize(30));

        return page.getContent();
    }

    // getting findById() -> customerRepository -> Has to be Optional<Customer> , we want to error handle if a
    // customer is null -> <Customer> is the class we want to grab the id from -> .orElseThrow is what will throw is
    // the customer is null
    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return (customerRepository.findById(id));
    }


    // person exist by id -> calling from the Repository
    @Override
    public boolean existsPersonWithId(Integer id) {
        return customerRepository.existsCustomerById(id);
    }

    // Updating customer based off of id -> using Repository and .save() method.
    @Override
    public void updateCustomer(Customer update) {
        customerRepository.save(update);
    }

    @Override
    public void updateCustomerProfileImageId(String profileImageId, Integer customerId) {
        customerRepository.updateCustomerProfileImageId(profileImageId, customerId);
    }

    @Override
    public Optional<Customer> selectCustomerByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    // deleting customer by id -> calling from Repository
    @Override
    public void deleteCustomerById(Integer customerId) {
        customerRepository.deleteById(customerId);
    }

    // Adding customer based off of id -> using Repository and .save() method.
    @Override
    public void insertCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    // Checking if the user exist via email
    @Override
    public boolean existsPersonWithEmail(String email) {
        return customerRepository.existsCustomerByEmail(email);
    }


}
