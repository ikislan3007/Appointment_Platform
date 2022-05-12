package com.example.appointmentApp.domain.appointment.mapper;

import com.example.appointmentApp.domain.activity.entity.Activity;
import com.example.appointmentApp.domain.activity.models.ActivityResponseDTO;
import com.example.appointmentApp.domain.appointment.entity.Appointment;
import com.example.appointmentApp.domain.appointment.models.AppointmentCreateDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentResponseDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentUpdateDTO;
import com.example.appointmentApp.domain.client.entity.Client;
import com.example.appointmentApp.domain.client.models.ClientResponseDTO;
import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.employee.model.EmployeeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    Appointment map(AppointmentCreateDTO appointmentCreateDTO);
    void update(@MappingTarget Appointment appointment, AppointmentUpdateDTO appointmentUpdateDTO);
    AppointmentResponseDTO map(Appointment appointment);
    ClientResponseDTO map(Client client);
    EmployeeResponseDTO map(Employee employee);
    ActivityResponseDTO map(Activity activity);
}
