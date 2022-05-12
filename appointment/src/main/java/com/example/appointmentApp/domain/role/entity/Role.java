package com.example.appointmentApp.domain.role.entity;

import com.example.appointmentApp.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"roleName"})})
public class Role extends BaseEntity implements Serializable {

    @NotNull(message = "Name cannot be null")
    @Size(min = 3, message = "Name must be at least 3 characters")
    @Column
    private String roleName;
    public Role (){}

    public Role (String roleName){
        this.roleName=roleName;
    }

    public Role(Long id,String roleName) {
        setId(id);
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
