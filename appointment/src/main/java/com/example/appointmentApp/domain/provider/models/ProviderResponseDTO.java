package com.example.appointmentApp.domain.provider.models;

import java.time.LocalTime;


public record ProviderResponseDTO(
        Long id,
        String name,
        String website,
        String domain,
        String phone,
        LocalTime startOfTheWorkingDay,
        LocalTime endOfTheWorkingDay,
        String workingDays) {
}
