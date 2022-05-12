package com.example.appointmentApp.infrastructure.custom.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NoAccessException extends RuntimeException {
    public NoAccessException(Long id){super("You do not have access to the account with id " + id );}
}
