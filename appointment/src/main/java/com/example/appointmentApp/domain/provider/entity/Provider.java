package com.example.appointmentApp.domain.provider.entity;

import com.example.appointmentApp.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.URL;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalTime;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Provider extends BaseEntity {
    @NotNull(message = "Name cannot be null")
    @Size(min = 3, message = "Name must be at least 3 characters")
    @Column(nullable = false)
    private String name;

    @URL(message = "Please, enter a valid URL")
    @NotEmpty(message = "Website cannot be empty")
    @Column(nullable = false)
    private String website;

    @Size(min = 2, message = "Domain must be at least 2 characters")
    @Pattern(regexp = "^[A-Za-z]*$",message = "Only letters")
    @Column(nullable = false)
    private String domain;

    @Pattern(regexp="^\\+(?:[0-9] ?){6,14}[0-9]$")
    @NotNull(message = "Phone cannot be null")
    @Column(nullable = false)
    private String phone;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(nullable = false)
    private LocalTime startOfTheWorkingDay;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(nullable = false)
    private LocalTime endOfTheWorkingDay;

    @Column(nullable = false)
    @NotNull(message = "Field working Days cannot be null")
    private String workingDays;

    public Provider(long Id, String name, String website, String domain, String phone, LocalTime startOfTheWorkingDay, LocalTime endOfTheWorkingDay, String workingDays) {
        setId(Id);
        this.name = name;
        this.website = website;
        this.domain = domain;
        this.phone = phone;
        this.startOfTheWorkingDay = startOfTheWorkingDay;
        this.endOfTheWorkingDay = endOfTheWorkingDay;
        this.workingDays=workingDays;
    }
    public Provider() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getWebsite() {
        return website;
    }
    public String getDomain() {
        return domain;
    }
    public void setDomain(String domain) {
        this.domain = domain;
    }
    public String getWorkingDays() {
        return workingDays;
    }
    public String getPhone() {
        return phone;
    }
    public LocalTime getEndOfTheWorkingDay() {
        return endOfTheWorkingDay;
    }
    public void setEndOfTheWorkingDay(LocalTime endOfTheWorkingDay) {
        this.endOfTheWorkingDay = endOfTheWorkingDay;
    }
    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }
    public LocalTime getStartOfTheWorkingDay() {
        return startOfTheWorkingDay;
    }
    public void setStartOfTheWorkingDay(LocalTime startOfTheWorkingDay) {
        this.startOfTheWorkingDay = startOfTheWorkingDay;
    }
}
