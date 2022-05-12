package com.example.appointmentApp.domain.activity.entity;

import com.example.appointmentApp.domain.BaseEntity;
import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.infrastructure.custom.activity.DurationToStringConverter;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.Duration;
import java.util.List;

@Entity
public class Activity extends BaseEntity {
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, message = "Name must be at least 2 characters")
    @Column(nullable = false)
    private String name;

    @Positive(message = "Price should be a positive decimal number")
    private double price;

    @NotNull(message = "Duration can not to be null")
    @Convert(converter = DurationToStringConverter.class)
    private Duration duration;

    @ManyToOne
    @JoinColumn(name = "providerId")
    private Provider provider;

    @NotEmpty(message = "Employees List cannot be empty.")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Employee> employeeList;

    public  Activity (){}
    public Activity(Long id,String name, double price, Duration duration, Provider provider,List<Employee> employeeList) {
        setId(id);
        this.name = name;
        this.price = price;
        this.duration=duration;
        this.provider=provider;
        this.employeeList=employeeList;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public Duration getDuration() {
        return duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }
    public Provider getProvider() {
        return provider;
    }
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
