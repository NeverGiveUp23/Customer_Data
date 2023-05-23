package com.felixvargas.customer.interfaces;

import com.felixvargas.customer.model.Customer;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CustomerDAO {

    List<Customer> selectAllCustomer();

    Optional<Customer> selectCustomerById(Integer customerId);

    void insertCustomer(Customer customer);

    boolean existsPersonWithEmail(String email);

    void deleteCustomerById(Integer customerId);

    boolean existsPersonWithId(Integer customerId);

    void updateCustomer(Customer update);

    void updateCustomerProfileImageId(String profileImageId, Integer customerId);

    Optional<Customer> selectCustomerByEmail(String email);
}
