package com.example.appointmentApp.domain.employee.service;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.employee.mapper.EmployeeMapper;
import com.example.appointmentApp.domain.employee.model.EmployeeCreateDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeResponseDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeUpdateDTO;
import com.example.appointmentApp.domain.employee.repository.EmployeeRepository;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.provider.repository.ProviderRepository;
import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.repository.RoleRepo;
import com.example.appointmentApp.infrastructure.custom.employee.NoAccessException;
import com.example.appointmentApp.infrastructure.custom.employee.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class EmployeeServiceImpl implements EmployeeService {
    private AccountRepository accountRepo;
    private ProviderRepository providerRepo;
    private EmployeeMapper employeeMapper;
    private EmployeeRepository employeeRepo;
    private RoleRepo roleRepo;

    @Override
    public EmployeeResponseDTO save(EmployeeCreateDTO employeeCreateDTO) {
        Role defaultRoleForUser = roleRepo.findByRoleName("EMPLOYEE");
        Account accountForCreation = accountRepo.save(employeeMapper.map(employeeCreateDTO.account()));
        accountForCreation.setRole(defaultRoleForUser);
        Provider providerForEmployee = providerRepo.findByName(employeeCreateDTO.providerName());
        Employee employeeForCreation = employeeMapper.map(employeeCreateDTO);
        employeeForCreation.setAccount(accountForCreation);
        employeeForCreation.setProvider(providerForEmployee);
        Employee employeeForSave = employeeRepo.save(employeeForCreation);
        return employeeMapper.map(employeeForSave);
    }

    @Override
    public EmployeeResponseDTO get(Long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee currentEmployee = employeeRepo.findByAccountEmail(auth.getName());
        if (currentEmployee.getId() == employee.getId() || (currentEmployee.getAccount().getRole().getRoleName().equals("PROVIDER_ADMIN") && currentEmployee.getProvider().getId() == employee.getProvider().getId())) {
            return employeeMapper.map(
                    employeeRepo.findById(id)
                            .orElseThrow(() -> new EmployeeNotFoundException(id)));
        } else {
            throw new NoAccessException(id);
        }
    }

    @Override
    public Page<EmployeeResponseDTO> getAll(Pageable pageable) {
            return employeeRepo
                    .findAll(pageable)
                    .map(employeeMapper::map);
    }

    @Override
    public EmployeeResponseDTO update(EmployeeUpdateDTO employeeUpdateDTO, Long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee currentEmployee = employeeRepo.findByAccountEmail(auth.getName());
        if (currentEmployee.getId() == employee.getId() || (currentEmployee.getAccount().getRole().getRoleName().equals("PROVIDER_ADMIN") && currentEmployee.getProvider().getId() == employee.getProvider().getId())) {
            Employee employeeDb = employeeRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
            employeeMapper.update(employeeDb, employeeUpdateDTO);
            return employeeMapper.map(employeeRepo.save(employeeDb));
        } else {
            throw new NoAccessException(id);
        }
    }

    @Override
    public Long delete(Long id) {
        Employee employee = employeeRepo.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee currentEmployee = employeeRepo.findByAccountEmail(auth.getName());
        if (currentEmployee.getAccount().getRole().getRoleName().equals("PROVIDER_ADMIN") && currentEmployee.getProvider().getId() == employee.getProvider().getId()) {
            if (employeeRepo.existsById(id)) {
                employeeRepo.deleteById(id);
            } else {
                throw new EmployeeNotFoundException(id);
            }
        } else {
            throw new NoAccessException(id);
        }
        return id;
    }


    @Autowired
    public void setAccountRepo(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Autowired
    public void setProviderRepo(ProviderRepository providerRepo) {
        this.providerRepo = providerRepo;
    }

    @Autowired
    public void setEmployeeMapper(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    @Autowired
    public void setEmployeeRepo(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Autowired
    public void setRoleRepo(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }
}



