package com.example.appointmentApp.domain.role.mapper;

import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.models.RoleCreateDTO;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role map(RoleCreateDTO roleCreateDTO);
    RoleResponseDTO map(Role role);

}
