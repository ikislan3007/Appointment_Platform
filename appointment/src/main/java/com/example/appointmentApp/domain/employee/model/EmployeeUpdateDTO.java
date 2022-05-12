package com.example.appointmentApp.domain.employee.model;

import javax.validation.constraints.*;

public record EmployeeUpdateDTO(@NotBlank(message = "Title cannot be blank")
                                @Size(min = 2, message = "Title must be at least 2 characters")
                                String title,

                                @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$")
                                @NotBlank(message = "Phone cannot be blank")
                                String phone,

                                @Positive(message = "Rate per hour should be a positive decimal number")
                                @NotNull(message = "Rate per hour cannot be null")
                                double ratePerHour) {
}
