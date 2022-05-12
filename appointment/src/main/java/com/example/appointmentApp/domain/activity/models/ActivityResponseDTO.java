package com.example.appointmentApp.domain.activity.models;

import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.provider.entity.Provider;

import java.time.Duration;
import java.util.List;

public record ActivityResponseDTO(Long id,
                                  String name,
                                  double price,
                                  Duration duration,
                                  Provider provider,
                                  List<Employee> employeeList) {
}
