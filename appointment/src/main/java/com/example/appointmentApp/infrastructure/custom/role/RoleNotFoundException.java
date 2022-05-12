package com.example.appointmentApp.infrastructure.custom.role;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RoleNotFoundException  extends RuntimeException {
    public RoleNotFoundException(Long id) {
        super("Role with id " + id + " doesn't exist.");
    }
}
