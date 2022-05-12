package com.example.appointmentApp.domain.client.models;

import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public record ClientCreateDTO(
        @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$")
        @NotBlank(message = "Phone cannot be blank")
        String phone,

        @NotBlank(message = "Phone cannot be blank")
        @Size(min = 3, message = "Address must be at least 3 characters")
        String address,
        AccountCreateDTO account) {
}
