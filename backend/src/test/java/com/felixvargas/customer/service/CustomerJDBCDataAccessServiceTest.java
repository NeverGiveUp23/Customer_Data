package com.felixvargas.customer.service;

import com.felixvargas.AbstractTestContainers;
import com.felixvargas.customer.customerMapper.CustomerRowMapper;
import com.felixvargas.customer.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {

    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();
    private CustomerJDBCDataAccessService underJDBCTest;

    @BeforeEach
    void setUp() {
        // Fresh instants of the customerJDBCDataAccessService for each test
        // before each test we get a new object
        underJDBCTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),  // <-
                customerRowMapper
        );
    }

    // inserting customer and getting customer refactor code
    private int insertAndGetCustomerId(Customer customer) {
        underJDBCTest.insertCustomer(customer);
        // retrieve all customers from the database and filter them by the email of the given customer
        // then return the ID of the first customer that matches the filter
        return underJDBCTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
    }


    @Test
    void selectAllCustomer() {
        // Given: create a new customer to insert into the database
        String name = Faker.name().fullName();  // generate a fake name
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(); // generate a fake email address
        Customer customer = new Customer(
                name,
                email,
                20 // set the age to 20
        );

        // insert the new customer into the database
        underJDBCTest.insertCustomer(customer);

        // When: get all customers from the database
        List<Customer> customers = underJDBCTest.selectAllCustomer();

        // Then: verify that the list of customers is not empty
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        // Generate a random email and name using the Faker library
        String email = Faker.internet().emailAddress() + "-" + UUID.randomUUID();
        String name = Faker.name().fullName();

        // Create a new customer with the generated email, name, and age of 20
        Customer customer = new Customer(
                name,
                email,
                20
        );

        // Insert the new customer into the database and get the auto-generated ID
        int id = insertAndGetCustomerId(customer);

        // When
        // Retrieve the customer from the database by ID
        Optional<Customer> actual = underJDBCTest.selectCustomerById(id);

        // Then
        // Assert that the retrieved customer matches the expected customer
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
        int id = 0;

        // When
        var actual = underJDBCTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }


    @Test
    void insertCustomer() {
        // Given
        // When

        // Then
    }

    @Test
    void existsPersonWithEmail() {
        // Given
        // Generate a random email and name using the Faker library
        String email = Faker.internet().emailAddress() + "-" + UUID.randomUUID();
        String name = Faker.name().fullName();

        Customer customer = new Customer(
                name,
                email,
                20
        );
        // Insert the new customer into the database and get the auto-generated ID
        underJDBCTest.insertCustomer(customer);
        // When
        // check if the email exists in the database
        boolean actual = underJDBCTest.existsPersonWithEmail(email);

        // Then return true is email in database
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenDoesNotExists() {
        // Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        // When
        boolean actual = underJDBCTest.existsPersonWithEmail(email);
        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );

        int id = insertAndGetCustomerId(customer);
        // When

        underJDBCTest.deleteCustomerById(id);

        // Then
        Optional<Customer> actual = underJDBCTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();
    }

    @Test
    void existsPersonWithId() {
        // Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );

        // Get the customer
        int id = insertAndGetCustomerId(customer);
        // When

        var actual = underJDBCTest.existsPersonWithId(id);


        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithIdWillReturnFalseWhenIdNotPresent() {
        // Given
        int id = -1;
        // When

        var actual = underJDBCTest.existsPersonWithId(id);


        // Then
        assertThat(actual).isFalse();
    }

    @Test
    void updateCustomer() {
        // Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );

        int id = insertAndGetCustomerId(customer);

        var newName = "Fel";
        // When
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underJDBCTest.updateCustomer(update);
        // Then
        Optional<Customer> actual = underJDBCTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);  // <- Change
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );

        int id = insertAndGetCustomerId(customer);

        var newEmail = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        // WHEN email is changed
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail);

        underJDBCTest.updateCustomer(update);
        // THEN test the email change
        Optional<Customer> actual = underJDBCTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(newEmail);  // <- Change
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                Faker.name().fullName(),
                email,
                20
        );

        int id = insertAndGetCustomerId(customer);

        var newAge = 100;

        // When age is changed
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underJDBCTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underJDBCTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(newAge); // change
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void updateAllCustomerProperties() {
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = Faker.name().fullName();

        Customer customer = new Customer(
                name,
                email,
                20
        );

        int id = insertAndGetCustomerId(customer);

        // WHEN All props are changed

        Customer update = new Customer();
        update.setId(id);
        update.setName("Gabe");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(44);

        underJDBCTest.updateCustomer(update);

        // THEN test the update changes

        Optional<Customer> actual = underJDBCTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void updateNotUpdateAllCustomerPropertiesWhenNothingToUpdate() {
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        String name = Faker.name().fullName();

        Customer customer = new Customer(
                name,
                email,
                20
        );

        int id = insertAndGetCustomerId(customer);

        // WHEN All props are changed

        Customer update = new Customer();
        update.setId(id);

        underJDBCTest.updateCustomer(update);

        // THEN test the update changes

        Optional<Customer> actual = underJDBCTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });
    }


}