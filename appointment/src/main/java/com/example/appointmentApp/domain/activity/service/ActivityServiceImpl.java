package com.example.appointmentApp.domain.activity.service;

import com.example.appointmentApp.domain.activity.entity.Activity;
import com.example.appointmentApp.domain.activity.mapper.ActivityMapper;
import com.example.appointmentApp.domain.activity.models.ActivityCreateDTO;
import com.example.appointmentApp.domain.activity.models.ActivityResponseDTO;
import com.example.appointmentApp.domain.activity.models.ActivityUpdateDTO;
import com.example.appointmentApp.domain.activity.repository.ActivityRepository;
import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.employee.repository.EmployeeRepository;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.provider.repository.ProviderRepository;
import com.example.appointmentApp.infrastructure.custom.activity.ActivityNotFoundException;
import com.example.appointmentApp.infrastructure.custom.activity.ProviderIsNotSameForException;
import com.example.appointmentApp.infrastructure.custom.employee.EmployeeNotFoundException;
import com.example.appointmentApp.infrastructure.custom.employee.NoAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {
    private ProviderRepository providerRepo;
    private ActivityMapper activityMapper;
    private ActivityRepository activityRepo;
    private EmployeeRepository employeeRepository;
    private static final String PROVIDER_ADMIN ="PROVIDER_ADMIN";

    @Override
    public ActivityResponseDTO save(ActivityCreateDTO activityCreateDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee currentProvider = employeeRepository.findByAccountEmail(auth.getName());

        List<Employee> employeeList = new ArrayList<>();
        activityCreateDTO.employeesIdList().forEach(id -> employeeList.add(employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id))));
        for (Employee employee : employeeList) {
            if (!(employee.getProvider().getName().equals(currentProvider.getProvider().getName()))) {
                throw new ProviderIsNotSameForException(currentProvider.getId());
            }
        }
        Provider providerForActivity = providerRepo.findByName(activityCreateDTO.providerName());
        Activity activityForCreation = activityMapper.map(activityCreateDTO);
        activityForCreation.setProvider(providerForActivity);
        activityForCreation.setEmployeeList(employeeList);
        Activity activityForSave = activityRepo.save(activityForCreation);
        return activityMapper.map(activityForSave);
    }

    @Override
    public ActivityResponseDTO update(ActivityUpdateDTO activityUpdateDTO, Long id) {
        Activity activity = activityRepo.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee currentEmployeeWithRoleProviderAdmin = employeeRepository.findByAccountEmail(auth.getName());

        if (currentEmployeeWithRoleProviderAdmin.getAccount().getRole().getRoleName().equals(PROVIDER_ADMIN)
                && activity.getProvider().getId().equals(currentEmployeeWithRoleProviderAdmin.getProvider().getId()) ){
            Activity activityDb = activityRepo.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
            List<Employee> employeeList = new ArrayList<>();
            activityUpdateDTO.employeesIdList().forEach(employeeId -> employeeList.add(employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EmployeeNotFoundException(employeeId))));
            for (Employee employee : employeeList) {
                if (!(employee.getProvider().getName().equals(currentEmployeeWithRoleProviderAdmin.getProvider().getName()))) {
                    throw new ProviderIsNotSameForException(currentEmployeeWithRoleProviderAdmin.getId());
                }
            }
            activityDb.setEmployeeList(employeeList);
            activityMapper.update(activityDb, activityUpdateDTO);
            return activityMapper.map(activityRepo.save(activityDb));
        } else {
            throw new NoAccessException(id);
        }
    }

    @Override
    public ActivityResponseDTO get(Long id) {
        Activity activity = activityRepo.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee currentEmployeeWithRoleProviderAdmin = employeeRepository.findByAccountEmail(auth.getName());

        if (currentEmployeeWithRoleProviderAdmin.getAccount().getRole().getRoleName().equals(PROVIDER_ADMIN)
                && activity.getProvider().getId().equals(currentEmployeeWithRoleProviderAdmin.getProvider().getId())) {
            return activityMapper.map(
                    activityRepo.findById(id)
                            .orElseThrow(() -> new ActivityNotFoundException(id)));
        } else {
            throw new NoAccessException(id);
        }
    }

    @Override
    public Page<ActivityResponseDTO> getAll(Pageable pageable) {
        return activityRepo
                .findAll(pageable)
                .map(activityMapper::map);
    }

    @Override
    public Long delete(Long id) {
        Activity activity = activityRepo.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Employee currentEmployeeWithRoleProviderAdmin = employeeRepository.findByAccountEmail(auth.getName());

        if (currentEmployeeWithRoleProviderAdmin.getAccount().getRole().getRoleName().equals(PROVIDER_ADMIN)
                && activity.getProvider().getId().equals(currentEmployeeWithRoleProviderAdmin.getProvider().getId())) {
            if (activityRepo.existsById(id)) {
                activityRepo.deleteById(id);
            } else {
                throw new ActivityNotFoundException(id);
            }
        } else {
            throw new NoAccessException(id);
        }
        return id;
    }

    @Autowired
    public void setProviderRepo(ProviderRepository providerRepo) {
        this.providerRepo = providerRepo;
    }

    @Autowired
    public void setActivityMapper(ActivityMapper activityMapper) {
        this.activityMapper = activityMapper;
    }

    @Autowired
    public void setActivityRepo(ActivityRepository activityRepo) {
        this.activityRepo = activityRepo;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
