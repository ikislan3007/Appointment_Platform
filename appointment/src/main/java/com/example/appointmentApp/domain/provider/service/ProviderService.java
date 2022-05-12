package com.example.appointmentApp.domain.provider.service;

import com.example.appointmentApp.domain.provider.models.ProviderCreateDTO;
import com.example.appointmentApp.domain.provider.models.ProviderResponseDTO;
import com.example.appointmentApp.domain.provider.models.ProviderUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProviderService {
    ProviderResponseDTO save(ProviderCreateDTO provider);
    ProviderResponseDTO get(Long id);
    Page<ProviderResponseDTO> getAll(Pageable pageable);
    ProviderResponseDTO update(ProviderUpdateDTO provider, Long id);
    Long delete(Long id);
}
