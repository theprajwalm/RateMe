package com.rateme.rateme.dto;

import com.rateme.rateme.Security.SecurityManager;

public record UserDTO(String username,
                      String firstname,
                      String lastname,
                      String email
                      ) {
}
