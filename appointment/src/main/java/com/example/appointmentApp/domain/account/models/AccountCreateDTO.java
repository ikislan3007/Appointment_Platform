package com.example.appointmentApp.domain.account.models;

import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.infrastructure.custom.account.password.ValidPassword;

import javax.validation.constraints.*;

public record AccountCreateDTO(@NotBlank(message = "FullName cannot be blank")
                               @Size(min = 3, message = "Name must be at least 3 characters")
                               String fullName,

                               @NotBlank(message = "Email cannot be blank")
                               @Email
                               String email,

                               @NotBlank(message = "Password cannot be null")
                               @Size(min = 8, message = "Password must be at least 8 characters")
                               @ValidPassword
                               String password,
                               Role role) {
}
