package com.felixvargas.customer.records;


import com.felixvargas.customer.enums.Gender;

public record CustomerRegReq(String name, String email, String password, Integer age, Gender gender) {

}
