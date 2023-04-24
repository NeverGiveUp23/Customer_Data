package com.felixvargas.customer.records;


import com.felixvargas.customer.enums.Gender;

public record CustomerRegReq(String name, String email, Integer age, Gender gender) {

}
