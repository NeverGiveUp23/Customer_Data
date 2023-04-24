package com.felixvargas.customer.enums;

public enum Gender {
    MALE,
    FEMALE,
    Male,
    Female;


    public static Gender fromString(String genderString) {
        return Gender.valueOf(genderString.toUpperCase());
    }

}

