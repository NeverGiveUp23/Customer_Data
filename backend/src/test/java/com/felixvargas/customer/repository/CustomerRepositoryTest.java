package com.felixvargas.customer.repository;

import com.felixvargas.AbstractTestContainers;
import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest // <- only loads what the JPA Component needs to run, brings in the essentials only
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // <- connects to our test containers
class CustomerRepositoryTest extends AbstractTestContainers {

    @Autowired
    private CustomerRepository underJPATest;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        underJPATest.deleteAll();
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        // Given: create a new customer to insert into the database
        String name = Faker.name().fullName();  // generate a fake name
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(); // generate a fake email address
        Customer customer = new Customer(
                name,
                email,
                20, // set the age to 20
                Gender.MALE);

        // insert the new customer into the database
        underJPATest.save(customer);

        // When: get all customers from the database
        var actual = underJPATest.existsCustomerByEmail(email);

        // Then: verify that the list of customers is not empty
        assertThat(actual).isTrue();
    }


    @Test
    void existsCustomerByEmailFailsIfNoneExists() {
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(); // generate a fake email address
        // When: get all customers from the database
        var actual = underJPATest.existsCustomerByEmail(email);

        // Then: verify that the list of customers is not empty
        assertThat(actual).isFalse();
    }


    @Test
    void existsCustomerById() {
        // Given: create a new customer to insert into the database
        String name = Faker.name().fullName();  // generate a fake name
        String email = Faker.internet().safeEmailAddress() + "-" + UUID.randomUUID(); // generate a fake email address
        Customer customer = new Customer(
                name,
                email,
                20, // set the age to 20
                Gender.MALE);

        // insert the new customer into the database
        underJPATest.save(customer);

        int id = underJPATest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When: get all customers from the database
        var actual = underJPATest.existsCustomerById(id);

        // Then: verify that the list of customers is not empty
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByIdNotPresent() {

        int id = -1;

        // When: get all customers from the database
        var actual = underJPATest.existsCustomerById(id);

        // Then: verify that the list of customers is not empty
        assertThat(actual).isFalse();
    }
}