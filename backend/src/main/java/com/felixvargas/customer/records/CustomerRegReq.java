package com.felixvargas.customer.records;

import com.felixvargas.customer.model.Gender;

public record CustomerRegReq(String name, String email, Integer age, Gender gender) {

}
