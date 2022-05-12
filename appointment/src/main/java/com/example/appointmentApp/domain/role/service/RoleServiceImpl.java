package com.example.appointmentApp.domain.role.service;

import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.mapper.RoleMapper;
import com.example.appointmentApp.domain.role.models.RoleCreateDTO;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.domain.role.repository.RoleRepo;
import com.example.appointmentApp.infrastructure.custom.role.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleMapper roleMapper;
    private RoleRepo roleRepo;

    @Override
    public RoleResponseDTO save(RoleCreateDTO role) {
        Role roleForSave = roleRepo.save(roleMapper.map(role));
        return roleMapper.map(roleForSave);
    }
    @Override
    public RoleResponseDTO get(Long id) {
        return roleMapper.map(roleRepo.findById(id).orElseThrow(() -> new RoleNotFoundException(id)));
    }
    @Override
    public Page<RoleResponseDTO> getAll(Pageable pageable) {
        return roleRepo
                .findAll(pageable)
                .map(roleMapper::map);
    }
    @Override
    public Long delete(Long id) {
        if (roleRepo.existsById(id)) {
            roleRepo.deleteById(id);
        } else {
            throw new RoleNotFoundException(id);
        }
        return id;
    }

    @Autowired
    public void setRoleMapper(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }
    @Autowired
    public void setRoleRepo(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }


}
