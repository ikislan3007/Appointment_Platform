package com.example.appointmentApp.domain.provider.service;

import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.employee.repository.EmployeeRepository;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.provider.mapper.ProviderMapper;
import com.example.appointmentApp.domain.provider.models.ProviderCreateDTO;
import com.example.appointmentApp.domain.provider.models.ProviderResponseDTO;
import com.example.appointmentApp.domain.provider.models.ProviderUpdateDTO;
import com.example.appointmentApp.domain.provider.repository.ProviderRepository;
import com.example.appointmentApp.infrastructure.custom.provider.ProviderExistEmployeesException;
import com.example.appointmentApp.infrastructure.custom.provider.ProviderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProviderServiceImpl implements ProviderService {
    private ProviderRepository providerRepo;
    private ProviderMapper providerMapper;
    private EmployeeRepository employeeRepository;

    @Override
    public ProviderResponseDTO save(ProviderCreateDTO provider) {
        Provider providerForCreation = providerMapper.map(provider);
        Provider providerForSave = providerRepo.save(providerForCreation);
        return providerMapper.map(providerForSave);
    }
    @Override
    public ProviderResponseDTO get(Long id) {
        Provider provider = providerRepo.findById(id).orElseThrow(() -> new ProviderNotFoundException(id));
        return providerMapper.map(provider);
    }

    @Override
    public Page<ProviderResponseDTO> getAll(Pageable pageable) {
        return providerRepo.findAll(pageable).map(providerMapper::map);
    }

    @Override
    public ProviderResponseDTO update(ProviderUpdateDTO provider, Long id) {
        Provider providerDB = providerRepo.findById(id).orElseThrow(() -> new ProviderNotFoundException(id));
        Provider providerForUpdate = providerMapper.map(provider);
        providerForUpdate.setId(providerDB.getId());
        return providerMapper.map(providerRepo.save(providerForUpdate));
    }

    @Override
    public Long delete(Long id) {
        List<Employee> employeesRelatedToProvider = employeeRepository.findByProviderId(id);
        if (!(employeesRelatedToProvider == null || employeesRelatedToProvider.size() == 0)) {
            throw new ProviderExistEmployeesException(id);
        } else {
            if (providerRepo.existsById(id)) {
                providerRepo.deleteById(id);
            } else {
                throw new ProviderNotFoundException(id);
            }
            return id;
        }
    }

    @Autowired
    public void setProviderMapper(ProviderMapper providerMapper) {
        this.providerMapper = providerMapper;
    }

    @Autowired
    public void setProviderRepo(ProviderRepository providerRepo) {
        this.providerRepo = providerRepo;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

}
