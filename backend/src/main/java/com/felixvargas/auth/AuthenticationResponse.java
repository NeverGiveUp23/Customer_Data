package com.felixvargas.auth;

import com.felixvargas.customer.DTO.CustomerDTO;

public record AuthenticationResponse(
        String token,
        CustomerDTO customerDTO
) {


}
