package com.example.appointmentApp.infrastructure.custom.appointment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class EmployeeCanNotAssignAnotherEmployeeException extends RuntimeException{
    public EmployeeCanNotAssignAnotherEmployeeException(Long id, Long anotherId){super("Employee with id"+ id+ "does not have access  to assign  another  Employee  with id"+anotherId);}
}
