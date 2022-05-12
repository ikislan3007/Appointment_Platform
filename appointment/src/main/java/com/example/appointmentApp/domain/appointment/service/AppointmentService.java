package com.example.appointmentApp.domain.appointment.service;

import com.example.appointmentApp.domain.appointment.models.AppointmentCreateDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentResponseDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {
    AppointmentResponseDTO saveWithRoleClient(AppointmentCreateDTO appointmentCreateDTO);
    AppointmentResponseDTO saveWithRoleEmployee(AppointmentCreateDTO appointmentCreateDTO);
    AppointmentResponseDTO saveWithRoleProviderAdmin(AppointmentCreateDTO appointmentCreateDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AppointmentResponseDTO updateWithRoleClient(AppointmentUpdateDTO appointmentUpdateDTO,Long appointmentId);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AppointmentResponseDTO updateWithRoleEmployee(AppointmentUpdateDTO appointmentUpdateDTO,Long appointmentId);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AppointmentResponseDTO updateWithRoleProviderAdmin(AppointmentUpdateDTO appointmentUpdateDTO,Long appointmentId);
    AppointmentResponseDTO get(Long id);
    Long delete(Long id);
    Page<AppointmentResponseDTO> getAll(Pageable pageable);


}
