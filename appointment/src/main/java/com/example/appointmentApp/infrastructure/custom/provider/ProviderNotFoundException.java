package com.example.appointmentApp.infrastructure.custom.provider;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProviderNotFoundException extends RuntimeException{
    public ProviderNotFoundException(Long id) {
        super("Provider with id " + id + " doesn't exist.");
    }
}

