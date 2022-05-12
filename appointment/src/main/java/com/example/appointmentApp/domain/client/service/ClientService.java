package com.example.appointmentApp.domain.client.service;

import com.example.appointmentApp.domain.client.models.ClientCreateDTO;
import com.example.appointmentApp.domain.client.models.ClientResponseDTO;
import com.example.appointmentApp.domain.client.models.ClientUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientService {
    ClientResponseDTO save(ClientCreateDTO clientCreateDTO);

    ClientResponseDTO get(Long id);

    Page<ClientResponseDTO> getAll(Pageable pageable);

    ClientResponseDTO update(ClientUpdateDTO clientUpdateDTO, Long id);

    Long delete(Long id);
}
