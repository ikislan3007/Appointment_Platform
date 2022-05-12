package com.example.appointmentApp.infrastructure.custom.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ClientNotFoundException  extends RuntimeException{
    public ClientNotFoundException(Long id) {
        super("Client with id " + id + " doesn't exist.");
    }
}

