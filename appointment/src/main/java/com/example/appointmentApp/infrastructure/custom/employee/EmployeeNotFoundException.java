package com.example.appointmentApp.infrastructure.custom.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class EmployeeNotFoundException extends RuntimeException  {
    public EmployeeNotFoundException(Long id){super("Employee with id " + id + " doesn't exist.");}

}
