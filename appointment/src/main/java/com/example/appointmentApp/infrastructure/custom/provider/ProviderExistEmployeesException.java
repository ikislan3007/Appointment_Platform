package com.example.appointmentApp.infrastructure.custom.provider;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProviderExistEmployeesException extends RuntimeException {
    public ProviderExistEmployeesException(Long id) { super("Provider with id " + id + " has related employees that need to be deleted first");}
}
