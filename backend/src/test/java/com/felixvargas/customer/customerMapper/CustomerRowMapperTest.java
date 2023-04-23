package com.felixvargas.customer.customerMapper;

import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.model.Gender;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("Jamill");
        when(resultSet.getString("email")).thenReturn("Jamill@aol.com");
        when(resultSet.getString("gender")).thenReturn("FEMALE");
        // When
        Customer actualCustomer = customerRowMapper.mapRow(resultSet, 1);

        // Then

        Customer expectedCustomer = new Customer(
                1, "Jamill", "Jamill@aol.com", 19,
                Gender.FEMALE);

        assertThat(actualCustomer).isEqualTo(expectedCustomer);
    }
}