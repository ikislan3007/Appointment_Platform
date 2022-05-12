package com.example.appointmentApp.infrastructure.custom.appointment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AppointmentNotFoundException extends RuntimeException {
    public AppointmentNotFoundException(Long id){super("Appointment with id" + id +  "doesn't exist.");}
}

