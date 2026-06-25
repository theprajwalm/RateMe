package com.rateme.rateme.dto;

public record UserDTO(String token,
                      String username,
                      String firstname,
                      String lastname,
                      String email) {
}
