package com.felixvargas.customer.customerMapper;

import com.felixvargas.customer.model.Customer;
import com.felixvargas.customer.enums.Gender;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class CustomerRowMapper implements RowMapper<Customer> {

    // Refactoring Row Mapper method from the service -> cleaner and organized code + easy for testing
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {

        return new Customer(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getInt("age"),
                Gender.valueOf(rs.getString("gender")),
                rs.getString("profile_image_id")
        );
    }
}
