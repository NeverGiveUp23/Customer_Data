package com.felixvargas.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
