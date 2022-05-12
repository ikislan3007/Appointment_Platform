package com.example.appointmentApp.infrastructure.custom.appointment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ProviderIsNotSameForEmployeeAndActivities  extends RuntimeException{
    public ProviderIsNotSameForEmployeeAndActivities(Long id){super("Provider with "+id+" is not same for employee and activities");}
}

