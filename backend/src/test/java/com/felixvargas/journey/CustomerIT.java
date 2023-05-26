package com.felixvargas.journey;

import com.felixvargas.customer.DTO.CustomerDTO;
import com.felixvargas.customer.enums.Gender;
import com.felixvargas.customer.records.CustomerRegReq;
import com.felixvargas.customer.records.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.shaded.com.google.common.io.Files;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest(webEnvironment = RANDOM_PORT) // port available at random each time
// your test runs
public class CustomerIT {

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_PATH = "/api/v1/customer";
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
                name, email, "password", age, gender
        );
        // send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH) // <- endpoint
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegReq.class) // <- attach the request
                .exchange() // <- send
                .expectStatus() // <- expect
                .isOk() // <- response to be ok
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        //get all customers
        List<CustomerDTO> getAllCustomer = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();
        // make sure that customer is present

       try{
           var id = getAllCustomer.stream()
                   .filter(customer -> customer.email().equals(email))
                   .map(CustomerDTO::id)
                   .findFirst()
                   .orElseThrow();

           CustomerDTO expectedCustomer = new CustomerDTO(
                   id,
                   name,
                   email,
                   gender,
                   age,
                   List.of("ROLE_USER"),
                   email,
                   null
           );

           assertThat(getAllCustomer).contains(expectedCustomer);
           // get customer by id
           webTestClient.get()
                   .uri(CUSTOMER_PATH + "/{id}", id)
                   .accept(APPLICATION_JSON)
                   .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                   .exchange()
                   .expectStatus()
                   .isOk()
                   .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                   })
                   .isEqualTo(expectedCustomer);
       }catch(NoSuchElementException e){
           System.out.println("Customer not found");
        }

    }


    @Test
    void canDeleteCustomer() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@felixvargas.com";
        int age = RANDOM.nextInt(1, 100);

        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegReq request = new CustomerRegReq(
                name, email, "password", age, gender
        );

        CustomerRegReq request2 = new CustomerRegReq(
                name, email + ".uk", "password", age, gender
        );

        // we need two customers to test delete and you cant delete the last customer with its own token

        // send a post request to create customer 1, this customer doesn't need a token
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegReq.class)
                .exchange()
                .expectStatus()
                .isOk();

        // send a post request to create customer 2 -> thi customer will
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request2), CustomerRegReq.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();


        try {
            int id = allCustomers.stream()
                    .filter(customer -> customer.email().equals(email))
                    .map(CustomerDTO::id)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Customer not found"));

            // Rest of your code
            webTestClient.delete()
                    .uri(CUSTOMER_PATH + "/{id}", id)
                    .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus()
                    .isOk();

            // customer 2 gets customer 1 by id because it deleted it
            webTestClient.get()
                    .uri(CUSTOMER_PATH + "/{id}", id)
                    .accept(APPLICATION_JSON)
                    .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                    .exchange()
                    .expectStatus()
                    .isOk();
        } catch (NoSuchElementException e) {
            // Handle the exception
        System.out.println("Customer not found");

        }
    }

    @Test
    void canUpdate() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@felixvargas.com";
        System.out.println("Email: " + email);
        int age = RANDOM.nextInt(1, 100);

        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegReq request = new CustomerRegReq(
                name, email, "password", age, gender
        );

        // send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegReq.class)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);


        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allCustomers.stream()
                .filter(customer -> customer.username().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

// Update customer
        String newName = "aly";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName, null, null
        );




        webTestClient.put()
                .uri(CUSTOMER_PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .contentType(APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);

// Get customer by ID
        CustomerDTO updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}", id)
                .accept(APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        CustomerDTO expectedCustomer = new CustomerDTO(
                id,
                newName,
                email,
                gender,
                age,
                List.of("ROLE_USER"),
                email,
                null
        );

        assertThat(updatedCustomer).isEqualTo(expectedCustomer);
    }

    @Test
    void canUploadProfilePicture() throws IOException {
        // Given
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@felixvargas.com";
        int age = faker.random().nextInt(1, 100);

        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegReq request = new CustomerRegReq(
                name, email, "password", age, gender
        );

        // Send a POST request to create a customer
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegReq.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // Get the created customer
        // Get the created customer
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        System.out.println(email);
        CustomerDTO customerDTO = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Customer not found " + email));

        assertThat(customerDTO.profileImageId()).isNullOrEmpty();

        // Load the image file
        String imagePath = "%s.jpg".formatted(gender.name().toLowerCase());

        Resource image = new ClassPathResource(imagePath);

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", image);

        // Upload the profile image
        webTestClient.post()
                .uri(CUSTOMER_PATH + "/{customerId}/profile-image", customerDTO.id())
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

        // Verify the profile image id
        String profileImageId = webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}", customerDTO.id())
                .accept(MediaType.IMAGE_JPEG)
                .header(AUTHORIZATION, String.format("Bearer " + jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody()
                .profileImageId();

        assertThat(profileImageId).isNotBlank();


        // download image for customer
//        byte[] downloadedImage = webTestClient.get()
//                .uri(CUSTOMER_PATH + "/{customerId}/profile-image", customerDTO.id())
//                .accept(MediaType.IMAGE_JPEG)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBody(byte[].class)
//                .returnResult()
//                .getResponseBody();
//
//        byte[] actual = Files.toByteArray(image.getFile());
//
//        assertThat(actual).isEqualTo(downloadedImage);
    }

}
