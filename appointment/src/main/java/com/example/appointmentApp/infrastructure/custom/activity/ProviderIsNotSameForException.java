package com.example.appointmentApp.infrastructure.custom.activity;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ProviderIsNotSameForException extends RuntimeException{
    public ProviderIsNotSameForException(Long id){super("Provider with "+id+" is not same for employees");}
}
