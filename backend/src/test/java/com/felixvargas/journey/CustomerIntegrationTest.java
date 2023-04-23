package com.felixvargas.journey;

import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.enums.Gender;
import com.felixvargas.customer.records.CustomerRegReq;
import com.felixvargas.customer.records.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // port available at random each time
// your test runs
public class CustomerIntegrationTest {

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customer";
    @Autowired
    private WebTestClient webTestClient;  // <- this will be used as out postman

    @Test
    void canRegisterACustomer() {
        // create registration request
        Faker faker = new Faker();  // <- bring in the Faker instance to create
        Name fakerName = faker.name(); // <- assign variable to a faker name
        String name = fakerName.fullName(); // <- generate a fake full name
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@animal123.com"; // <- generate random last
        // name
        // for email
        int age = RANDOM.nextInt(1, 100); // <- random age
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        // create and new registration request customer
        CustomerRegReq request = new CustomerRegReq(
                name, email, age, gender
        );
        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI) // <- endpoint
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegReq.class) // <- attach the request
                .exchange() // <- send
                .expectStatus() // <- expect
                .isOk(); // <- response to be ok
        //get all customers
        List<Customer> getAllCustomer = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();
        // make sure that customer is present
        Customer expectedCustomer = new Customer(
                name, email, age,
                gender);
        assertThat(getAllCustomer).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        assert getAllCustomer != null;
        var id = getAllCustomer.stream().filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);
        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);

    }


    @Test
    void canDeleteCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@animal123.com";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegReq request = new CustomerRegReq(
                name, email, age, gender
        );

        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegReq.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        assert allCustomers != null;
        int id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // delete customer

        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void canUpdate() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@animal123.com";
        int age = RANDOM.nextInt(1, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
        CustomerRegReq request = new CustomerRegReq(
                name, email, age, gender
        );

        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegReq.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        assert allCustomers != null;
        int id = allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // update customer

        String newName = "aly";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName, null, null
        );

        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        Customer updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();


        Customer expectedCustomer = new Customer(
                id, newName, email, age,
                gender);

        assertThat(updatedCustomer).isEqualTo(expectedCustomer);
    }
}
