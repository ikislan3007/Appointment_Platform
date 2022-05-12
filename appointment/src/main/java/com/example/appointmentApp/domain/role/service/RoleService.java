package com.example.appointmentApp.domain.role.service;

import com.example.appointmentApp.domain.role.models.RoleCreateDTO;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    RoleResponseDTO save(RoleCreateDTO role) ;
    RoleResponseDTO get(Long id);
    Page<RoleResponseDTO> getAll(Pageable pageable);
    Long delete(Long id);
}
