package com.felixvargas.customer.service;

import com.felixvargas.customer.customerMapper.CustomerRowMapper;
import com.felixvargas.customer.interfaces.CustomerDAO;
import com.felixvargas.customer.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDAO {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    // Constructor for initializing jdbcTemplate and customerRowMapper
    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    // Retrieve all customers from the database
    @Override
    public List<Customer> selectAllCustomer() {
        // SQL query to select all customers
        var sql = """
                SELECT id, name, email, age
                FROM customer
                """;
        // Execute the query and map the result set to a list of Customer objects
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    // Retrieve a customer by its id
    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        // SQL query to select a customer by id
        var sql = """
                SELECT id, name, email, age FROM customer WHERE id = ?
                """;
        // Execute the query with the id as a parameter and map the result to a Customer object
        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    // Insert a new customer into the database
    @Override
    public void insertCustomer(Customer customer) {
        // SQL query to insert a new customer
        var sql = """
                INSERT INTO customer(name, email, age) VALUES (?, ?, ?)
                 """;
        // Execute the query with the customer's name, email, and age as parameters
        var result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge()
        );
        System.out.println("jdbcTemplate.update = " + result);
    }

    // Check if a customer with the given email exists in the database
    @Override
    public boolean existsPersonWithEmail(String email) {
        // SQL query to count customers with the given email
        var sql = """
                SELECT count(id) FROM customer WHERE email = ?
                """;
        // Execute the query with the email as a parameter
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }


    // Delete a customer by its id
    @Override
    public void deleteCustomerById(Integer customerId) {
        var sql = """
                DELETE
                FROM customer
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, customerId);
    }

    // Check if a customer with the given id exists in the database
    @Override
    public boolean existsPersonWithId(Integer id) {
        // SQL query to count customers with the given id
        var sql = """
                SELECT count(id) FROM customer WHERE id = ?
                """;
        // Execute the query with the id as a parameter
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    // Update a customer's information in the database
    @Override
    public void updateCustomer(Customer update) {
        // Check if the name field is not null and update the name
        if (update.getName() != null) {
            // SQL query to update the customer's name
            String sql = "UPDATE customer SET name = ? WHERE id = ?";
            // Execute the query with the new name and customer id as parameters
            int result = jdbcTemplate.update(sql, update.getName(), update.getId());
            System.out.println("Update Customer Name Result = " + result);
        }

        // Check if the email field is not null and update the email
        if (update.getEmail() != null) {
            // SQL query to update the customer's email
            String sql = "UPDATE customer SET email= ? WHERE id = ?";
            // Execute the query with the new email and customer id as parameters
            jdbcTemplate.update(sql, update.getEmail(), update.getId());
        }

        // Check if the age field is not null and update the age
        if (update.getAge() != null) {
            // SQL query to update the customer's age
            String sql = "UPDATE customer SET age = ? WHERE id = ?";
            // Execute the query with the new age and customer id as parameters
            jdbcTemplate.update(sql, update.getAge(), update.getId());
        }

    }
}
