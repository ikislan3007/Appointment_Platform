package com.example.appointmentApp.infrastructure.custom.appointment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NoAccessToAppointmentException extends RuntimeException{
    public NoAccessToAppointmentException(Long id){super("You do not have access to the appointment with id " + id );}}

