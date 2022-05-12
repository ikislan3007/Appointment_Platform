package com.example.appointmentApp.domain.appointment.repository;

import com.example.appointmentApp.domain.appointment.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository  extends JpaRepository<Appointment,Long> {
    Page<Appointment> findByClientId(Long clientId, Pageable pageable);
    Page<Appointment> findByEmployeeId(Long employeeId, Pageable pageable);
}
