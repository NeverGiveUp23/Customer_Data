package com.felixvargas.jwt;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;
import java.util.Map;


public class CustomUser extends User {
    private final String name;

    public CustomUser(String username, String password,
                      Collection<? extends GrantedAuthority> authorities,
                      String name) {
        super(username, password, authorities);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getAdditionalAttributes() {
        return Map.of("name", name);
    }
}
