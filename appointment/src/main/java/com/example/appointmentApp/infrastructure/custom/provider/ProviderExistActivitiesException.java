package com.example.appointmentApp.infrastructure.custom.provider;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProviderExistActivitiesException extends RuntimeException{
    public ProviderExistActivitiesException(Long id) { super("Provider with id " + id + " has related activities that need to be deleted first");}
}

