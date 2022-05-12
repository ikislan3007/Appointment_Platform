package com.example.appointmentApp.domain.employee.service;

import com.example.appointmentApp.domain.employee.model.EmployeeCreateDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeResponseDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface EmployeeService {
   EmployeeResponseDTO save(EmployeeCreateDTO employeeCreateDTO);
   EmployeeResponseDTO get(Long id);
   Page<EmployeeResponseDTO> getAll(Pageable pageable);
   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   EmployeeResponseDTO update(EmployeeUpdateDTO employeeUpdateDTO, Long id);
   Long delete(Long id);
}
