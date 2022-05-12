package com.example.appointmentApp.domain.client.entity;

import com.example.appointmentApp.domain.BaseEntity;
import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.appointment.entity.Appointment;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
public class Client extends BaseEntity {
    @Pattern(regexp="^\\+(?:[0-9] ?){6,14}[0-9]$")
    @NotBlank(message = "Phone cannot be blank")
    @Column(nullable = false)
    private String phone;

    @NotBlank(message = "Phone cannot be blank")
    @Size(min = 3, message = "Address must be at least 3 characters")
    @Column(nullable = false)
    private String address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private List<Appointment> appointments;
    public Client (){}
    public Client(Long id,String phone, String address, Account account) {
        setId(id);
        this.phone = phone;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
