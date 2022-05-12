package com.example.appointmentApp.domain.account.models;

import com.example.appointmentApp.infrastructure.custom.account.password.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record AccountUpdateDTO(@NotBlank(message = "FullName cannot be blank")
                               @Size(min = 3, message = "Name must be at least 3 characters")
                               String fullName,

                               @NotBlank(message = "Email cannot be blank")
                               @Email
                               String email,

                               @NotBlank(message = "Password cannot be null")
                               @Size(min = 3, message = "Password must be at least 8 characters")
                               @ValidPassword
                               String password) {
}
