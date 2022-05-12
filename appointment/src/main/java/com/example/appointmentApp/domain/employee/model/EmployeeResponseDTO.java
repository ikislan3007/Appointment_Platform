package com.example.appointmentApp.domain.employee.model;

import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.provider.entity.Provider;


public record EmployeeResponseDTO(Long id,
                                  String title,
                                  String phone,
                                  double ratePerHour,
                                  Provider provider,
                                  AccountResponseDTO account) {
}
