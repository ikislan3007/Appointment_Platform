package com.example.appointmentApp.domain.activity.models;

import javax.validation.constraints.*;
import java.time.Duration;
import java.util.List;

public record ActivityUpdateDTO(@NotBlank(message = "Name cannot be blank")
                                @Size(min = 2, message = "Name must be at least 2 characters")
                                String name,

                                @Positive(message = "Price should be a positive decimal number")
                                double price,

                                @NotNull(message = "Duration can not to be null")
                                Duration duration,

                                @NotEmpty(message = "Employees List cannot be empty.")
                                List<Long> employeesIdList) {
}
