package com.example.appointmentApp.domain.appointment.models;

import com.example.appointmentApp.domain.activity.models.ActivityResponseDTO;
import com.example.appointmentApp.domain.client.models.ClientResponseDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeResponseDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.Set;

public record AppointmentResponseDTO(Long id,
                                     LocalDateTime startDate,
                                     LocalDateTime endDate,
                                     ClientResponseDTO client,
                                     EmployeeResponseDTO employee,
                                     @JsonIgnoreProperties("employeeList")
                                     Set<ActivityResponseDTO> activities,
                                     double price) {
}
