package com.felixvargas.customer.DTO;

import com.felixvargas.customer.enums.Gender;

import java.util.List;

// DTO = Data Transfer Object
public record CustomerDTO(
        Integer id,
        String name,
        String email,
        Gender gender,
        Integer age,
        List<String> roles,
        String username,
        String profileImageId
) {


}
