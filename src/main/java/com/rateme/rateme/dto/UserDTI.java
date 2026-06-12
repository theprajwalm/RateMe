package com.rateme.rateme.dto;

public record UserDTI(String username,
                      String password,
                      String firstname,
                      String lastname,
                      String email,
                      String street,
                      String street_nr,
                      int zip,
                      String city) {
}
