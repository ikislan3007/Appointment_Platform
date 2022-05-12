package com.example.appointmentApp.domain.client.models;

import com.example.appointmentApp.domain.account.models.AccountResponseDTO;

public record ClientResponseDTO(
        Long id,
        String phone,
        String address,
        AccountResponseDTO account) {
}
