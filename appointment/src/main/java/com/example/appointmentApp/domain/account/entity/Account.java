package com.example.appointmentApp.domain.account.entity;

import com.example.appointmentApp.domain.BaseEntity;
import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.infrastructure.custom.account.password.ValidPassword;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Entity
public class Account extends BaseEntity implements Serializable {
    @NotNull(message = "FullName cannot be null")
    @Size(min = 3, message = "Name must be at least 3 characters")
    private String fullName;

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @ValidPassword
    private String password;

    @ManyToOne
    @JoinColumn(name = "roleId")
    private Role role;

    public Account() {
    }

    public Account(Long id, String fullName, String email, String password) {
        setId(id);
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public Account(Long id, String fullName, String email, String password, Role role) {
        setId(id);
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role=role;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
