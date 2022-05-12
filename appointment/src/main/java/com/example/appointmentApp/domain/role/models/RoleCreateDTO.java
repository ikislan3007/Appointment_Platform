package com.example.appointmentApp.domain.role.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record RoleCreateDTO(@NotBlank(message = "RoleName cannot be blank")
                            @Size(min = 2, message = "Name must be at least 3 characters")
                            String roleName) {
}
