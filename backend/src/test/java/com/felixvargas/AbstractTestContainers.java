package com.felixvargas;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;


// Use the Testcontainers library to create a containerized environment for testing
// This annotation will automatically start and stop the containers
@Testcontainers
public abstract class AbstractTestContainers {

    // Define a static instance of a PostgreSQL container with specific database, username, and password
    // The container will be automatically started and stopped with the test
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("customer-api:latest")
                    .withDatabaseName("felixvargas-dao-unit-test")
                    .withUsername("felix")
                    .withPassword("password");
    protected static final Faker Faker = new Faker();

    // Use the Flyway library to run database migrations before running the tests
    @BeforeAll
    static void beforeAll() {
        // Create a Flyway instance using the PostgreSQLContainer's database URL, username, and password
        Flyway flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        ).load();
        // Run the database migrations
        flyway.migrate();
        System.out.println("Done");
    }

    // Use DynamicPropertySource to map the application.yml properties to the test containers
    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                postgreSQLContainer::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                postgreSQLContainer::getUsername
        );
        registry.add(
                "spring.datasource.password",
                postgreSQLContainer::getPassword
        );
    }

    private static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}

