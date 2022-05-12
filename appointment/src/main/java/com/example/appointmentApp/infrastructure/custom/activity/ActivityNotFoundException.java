package com.example.appointmentApp.infrastructure.custom.activity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ActivityNotFoundException  extends RuntimeException  {
    public ActivityNotFoundException(Long id){super("Activity with id " + id + " doesn't exist.");}

}
