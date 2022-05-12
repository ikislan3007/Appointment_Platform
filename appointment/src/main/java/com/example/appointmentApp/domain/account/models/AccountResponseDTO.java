package com.example.appointmentApp.domain.account.models;

import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;


public record AccountResponseDTO(Long id,
                                 String fullName,
                                 String email,
                                 @JsonIgnore
                                 String password,
                                 RoleResponseDTO role) {

}
