package com.example.appointmentApp.infrastructure.custom.role;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RoleNameAlreadyExistsException extends RuntimeException {
    public RoleNameAlreadyExistsException(String roleNme){super("Role with name " +roleNme+" already  exists.");
    }
}
