package com.felixvargas;

import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.enums.Gender;
import com.felixvargas.customer.repository.CustomerRepository;
import com.felixvargas.s3.S3Buckets;
import com.felixvargas.s3.S3Service;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }


    @Bean
    CommandLineRunner runner(
            CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            createRandomCustomer(customerRepository, passwordEncoder);
//              getBucketDownloadTest(s3Service, s3Buckets);
        };
    }

    private static void getBucketDownloadTest(S3Service s3Service, S3Buckets s3Buckets) {
        s3Service.putObject(
                s3Buckets.getCustomer(),
                "customer",
                "Hello World".getBytes()
                );

        byte[] object = s3Service.getObject(
                "fs-customer-data-system-test",
                "customer"
        );

        System.out.println("Hooray: " + new String(object));
    }

    private static void createRandomCustomer(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        Faker faker = new Faker();
        Name name = faker.name();
        String firstName = name.firstName();
        String lastName = name.lastName();
        Random random = new Random();
        String email = firstName.toLowerCase() + "-" + lastName.toLowerCase() + "@felixvargas.com";
        int age = random.nextInt(16, 99);

        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                passwordEncoder.encode(UUID.randomUUID().toString()), age,
                gender);
        customerRepository.save(customer);
    }

}
