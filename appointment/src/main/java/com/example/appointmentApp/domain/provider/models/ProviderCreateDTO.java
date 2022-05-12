package com.example.appointmentApp.domain.provider.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.URL;
import javax.validation.constraints.*;
import java.time.LocalTime;

public record ProviderCreateDTO(
        @NotNull(message = "Name cannot be null")
        @Size(min = 3, message = "Name must be at least 3 characters")
        String name,

        @URL(message = "Please, enter a valid URL")
        @NotEmpty(message = "Website cannot be empty")
        String website,

        @Size(min = 2, message = "Domain must be at least 2 characters")
        @Pattern(regexp = "^[A-Za-z]*$", message = "Only letters")
        String domain,

        @Pattern(regexp="^\\+(?:[0-9] ?){6,14}[0-9]$")
        @NotNull(message = "Phone cannot be null")
        String  phone,

        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime startOfTheWorkingDay,

        @JsonFormat(pattern = "HH:mm:ss")
        LocalTime endOfTheWorkingDay,

        @NotNull(message = "Field working Days cannot be null")
        String workingDays) {
}
