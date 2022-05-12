package com.example.appointmentApp.domain.employee.entity;

import com.example.appointmentApp.domain.BaseEntity;
import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.appointment.entity.Appointment;
import com.example.appointmentApp.domain.provider.entity.Provider;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
public class Employee  extends BaseEntity {
    @NotBlank(message = "Title cannot be blank")
    @Size(min = 2, message = "Title must be at least 2 characters")
    @Column(nullable = false)
    private String title;

    @Pattern(regexp="^\\+(?:[0-9] ?){6,14}[0-9]$")
    @NotBlank(message = "Phone cannot be blank")
    @Column(nullable = false)
    private String phone;

    @Positive(message = "Rate per hour should be a positive decimal number")
    @NotNull
    private double ratePerHour;

    @ManyToOne
    @JoinColumn(name = "providerId")
    private Provider provider;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<Appointment> appointments;

    public Employee (){}
    public Employee(Long id,String title, String phone, double ratePerHour, Provider provider,Account account) {
        setId(id);
        this.title =title;
        this.phone=phone;
        this.ratePerHour = ratePerHour;
        this.provider=provider;
        this.account=account;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public double getRatePerHour() {
        return ratePerHour;
    }
    public void setRatePerHour(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }
    public Provider getProvider() {
        return provider;
    }
    public void setProvider(Provider provider) {
        this.provider = provider;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
