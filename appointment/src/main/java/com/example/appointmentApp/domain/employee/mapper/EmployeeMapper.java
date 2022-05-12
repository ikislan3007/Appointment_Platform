package com.example.appointmentApp.domain.employee.mapper;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.employee.model.EmployeeCreateDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeResponseDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeUpdateDTO;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.provider.models.ProviderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee map(EmployeeCreateDTO employeeCreateDTO);
    Account map(AccountCreateDTO accountCreateDTO);
    Employee map(EmployeeUpdateDTO employeeUpdateDTO);
    EmployeeResponseDTO map(Employee employee);
    AccountResponseDTO map(Account account);
    ProviderResponseDTO map(Provider provider);
    void update(@MappingTarget Employee entity, EmployeeUpdateDTO updateEntity);
}
