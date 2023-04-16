package com.felixvargas;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


//@SpringBootTest  // <- never use this annotation for Unit test <- you are focused on data Access layer,
// this will slow your test and bring in unnecessary beans.
// Extend the AbstractTestContainers class to use the containerized environment for testing
public class TestContainersTest extends AbstractTestContainers {

    // Define the test method
    @Test
    void canStartPostgresDB() {
        // Use AssertJ to verify that the container is running
        assertThat(postgreSQLContainer.isRunning()).isTrue();

        // Use AssertJ to verify that the container has been created successfully
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
