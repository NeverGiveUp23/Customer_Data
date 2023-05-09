package com.felixvargas.journey;

import com.felixvargas.auth.AuthenticationRequest;
import com.felixvargas.auth.AuthenticationResponse;
import com.felixvargas.customer.DTO.CustomerDTO;
import com.felixvargas.customer.enums.Gender;
import com.felixvargas.customer.records.CustomerRegReq;
import com.felixvargas.jwt.JWTUtil;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // port available at random each time
// your test runs
public class AuthenticationIT {

    private static final Random RANDOM = new Random();
    private static final String AUTHENTICATION_PATH = "/api/v1/auth";
    private static final String CUSTOMER_PATH = "/api/v1/customer";
    @Autowired
    private WebTestClient webTestClient;  // <- this will be used as out postman
    @Autowired
    private JWTUtil jwtUtil;



    @Test
    void canLogin() {
        // create registration customerRegReq
        Faker faker = new Faker();  // <- bring in the Faker instance to create
        Name fakerName = faker.name(); // <- assign variable to a faker name
        String name = fakerName.fullName(); // <- generate a fake full name
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@animal123.com"; // <- generate random last
        // name
        // for email
        int age = RANDOM.nextInt(1, 100); // <- random age
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        String password = "password";

        // create and new registration customerRegReq customer
        CustomerRegReq customerRegReq = new CustomerRegReq(
                name, email, "password", age, gender
        );

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(
                email,
                password
        );

        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        // send a post customerRegReq
        webTestClient.post()
                .uri(CUSTOMER_PATH) // <- endpoint
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(customerRegReq), CustomerRegReq.class) // <- attach the customerRegReq
                .exchange() // <- send
                .expectStatus() // <- expect
                .isOk(); // <- response to be ok


        EntityExchangeResult<AuthenticationResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthenticationResponse>() {
                })
                .returnResult();


        String jwtToken = result
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);


        AuthenticationResponse authenticationResponse = result.getResponseBody();

        CustomerDTO customerDTO = authenticationResponse.customerDTO();

        assertThat(jwtUtil.isTokenValid(jwtToken, customerDTO.username())).isTrue();

        assertThat(customerDTO.email()).isEqualTo(email);
        assertThat(customerDTO.age()).isEqualTo(age);
        assertThat(customerDTO.name()).isEqualTo(name);
        assertThat(customerDTO.username()).isEqualTo(email);
        assertThat(customerDTO.gender()).isEqualTo(gender);
        assertThat(customerDTO.roles()).isEqualTo(List.of("ROLE_USER"));


    }
    }


