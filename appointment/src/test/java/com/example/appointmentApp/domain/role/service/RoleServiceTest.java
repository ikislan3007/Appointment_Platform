package com.example.appointmentApp.domain.role.service;

import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.mapper.RoleMapper;
import com.example.appointmentApp.domain.role.models.RoleCreateDTO;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.domain.role.repository.RoleRepo;
import com.example.appointmentApp.infrastructure.custom.role.RoleNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {
    @InjectMocks
    private RoleServiceImpl roleService;
    @Mock
    private RoleMapper roleMapper;
    @Mock
    private RoleRepo roleRepo;
    Role role;
    RoleResponseDTO roleResponseDTO;

    @Before
    public void setUp() {
        role = new Role(1L, "user");
        roleResponseDTO = new RoleResponseDTO(1L, "Admin");
    }

    @Test
    public void getRoleByIdTest() {
        doReturn(Optional.of(role)).when(roleRepo).findById(role.getId());
        doReturn(roleResponseDTO).when(roleMapper).map(role);
        RoleResponseDTO roleResponseDTOFromService = this.roleService.get(1L);
        assertThat(roleResponseDTOFromService.id()).isEqualTo(role.getId());
    }

    @Test
    public void getRoleByIdNotExistingIdShouldReturnRoleNotFoundException() {
        Mockito.when(roleRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RoleNotFoundException.class, () -> roleService.get(1L));
    }

    @Test
    public void findAllRoles() {
        int pageNumber = 0;
        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Role> rolePage = new PageImpl<>(Collections.singletonList(role));
        when(roleRepo.findAll(pageable)).thenReturn(rolePage);
        Page<Role> roles= roleRepo.findAll(pageable);
        assertEquals(1,roles.getNumberOfElements());
    }

    @Test
    public void deleteRoleTest() {
        Mockito.when(roleRepo.existsById(role.getId())).thenReturn(true);
        doNothing().when(roleRepo).delete(role);
        roleService.delete(role.getId());
        assertThat(roleService.delete(role.getId())).isEqualTo(role.getId());
    }

    @Test
    public void deleteRoleShouldReturnRoleNotFoundException() {
        Mockito.when(roleRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RoleNotFoundException.class, () -> roleService.delete(1L));

    }

    @Test
    public void createRoleTest() {
        RoleCreateDTO roleForCreateDto = new RoleCreateDTO("user");
        doReturn(role).when(roleMapper).map(roleForCreateDto);
        Role createdRole = new Role(1L, "user");
        doReturn(createdRole).when(roleRepo).save(role);
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "user");
        doReturn(roleResponseDTO).when(roleMapper).map(createdRole);
        RoleResponseDTO roleResponseDTOForTest = roleService.save(roleForCreateDto);

        assertThat(roleResponseDTOForTest.roleName()).isEqualTo(roleForCreateDto.roleName());
    }
}
