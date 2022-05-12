package com.example.appointmentApp.domain.appointment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record AppointmentCreateDTO(@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                   LocalDateTime startDate,
                                   @NotNull(message = "Employee ID can not to be null")
                                   Long employeeId,
                                   @NotEmpty(message = "Activities cannot be empty.")
                                   List<Long> activitiesId,
                                   Long clientId) {
}
