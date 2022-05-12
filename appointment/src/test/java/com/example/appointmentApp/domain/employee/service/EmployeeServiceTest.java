package com.example.appointmentApp.domain.employee.service;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
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
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.domain.role.repository.RoleRepo;
import com.example.appointmentApp.infrastructure.custom.employee.EmployeeNotFoundException;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    @Mock
    private EmployeeRepository employeeRepo;
    @Mock
     private EmployeeMapper employeeMapper;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    private AccountRepository accountRepo;
    @Mock
    private ProviderRepository providerRepo;
    @Mock
    Authentication auth;
    AccountResponseDTO accountResponseDTO;
    RoleResponseDTO roleResponseDTO;
    Employee employee;
    Account account;
    Role role;
    Provider provider;

    @Before
    public void setUp() {
        role = new Role(1L, "account");
        SecurityContextHolder.getContext().setAuthentication(auth);
        roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",roleResponseDTO);
        account = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",role);
        provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        employee=new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
    }

    @Test
    public void getEmployeeByIdTest() {
        Employee currentEmployee= new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        Mockito.when(employeeRepo.findByAccountEmail(auth.getName())).thenReturn(currentEmployee);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L,"employee", "+1 1234567810123", 5.00,provider,  accountResponseDTO);
        doReturn(Optional.of(employee)).when(employeeRepo).findById(employee.getId());
        doReturn(employeeResponseDTO).when(employeeMapper).map(employee);
        EmployeeResponseDTO employeeResponseDTOFromService = this.employeeService.get(1L);
        assertThat(employeeResponseDTOFromService.id()).isEqualTo(employee.getId());
    }

    @Test
    public void getEmployeeByIdNotExistingIdShouldReturnEmployeeNotFoundException() {
        Mockito.when(employeeRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.get(1L));
    }

    @Test
    public void findAllProviders() {
        int pageNumber = 0;
        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Employee> emplPage = new PageImpl<>(Collections.singletonList(employee));
        when(employeeRepo.findAll(pageable)).thenReturn(emplPage);
        Page<Employee> employees = employeeRepo.findAll(pageable);
        assertEquals(1, employees.getNumberOfElements());
    }

    @Test
    public void deleteEmployeeTest() {
        Mockito.when(employeeRepo.findById(1L)).thenReturn(Optional.ofNullable(employee));
        Employee currentEmployee= new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        Mockito.when(employeeRepo.findByAccountEmail(auth.getName())).thenReturn(currentEmployee);
        Mockito.when(employeeRepo.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepo).delete(employee);
        employeeService.delete(employee.getId());
        assertThat(employeeService.delete(employee.getId())).isEqualTo(employee.getId());
    }

    @Test
    public void deleteEmployeeShouldReturnEmployeeNotFoundException() {
        Mockito.when(employeeRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(EmployeeNotFoundException.class, () -> employeeService.delete(1L));
    }

    @Test
    public void createEmployeeTest() {
        doReturn(account).when(accountRepo).save(any());
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO("Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", role);
        EmployeeCreateDTO employeeCreateDTO = new EmployeeCreateDTO("employee", "+1 1234567810123", 5.00,"provider",  accountCreateDTO);
        doReturn(employee).when(employeeMapper).map(employeeCreateDTO);
        doReturn(provider).when(providerRepo).findByName(any());
        Employee createdEmployee = new Employee(1L, "employee", "+1 1234567810123", 5.00,provider,account);
        doReturn(createdEmployee).when(employeeRepo).save(employee);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L,"employee", "+1 1234567810123", 5.00,provider,  accountResponseDTO);
        doReturn(employeeResponseDTO).when(employeeMapper).map(createdEmployee);
        EmployeeResponseDTO employeeResponseDTOForTest = employeeService.save(employeeCreateDTO);
        assertThat(employeeResponseDTOForTest.title()).isEqualTo(employeeCreateDTO.title());
    }
    @Test
    public void updateEmployeeTest() {
        Employee currentEmployee= new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        Mockito.when(employeeRepo.findByAccountEmail(auth.getName())).thenReturn(currentEmployee);
        EmployeeUpdateDTO employeeUpdateDTO = new EmployeeUpdateDTO("employee", "+1 1234567810123", 5.00);
        Employee employeeForUpdate = new Employee( 1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        doReturn(Optional.of(employeeForUpdate)).when(employeeRepo).findById(1L);
        doNothing().when(employeeMapper).update(employeeForUpdate,employeeUpdateDTO);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L,"employee", "+1 1234567810123", 5.00,provider,  accountResponseDTO);
        doReturn(employeeResponseDTO).when(employeeMapper).map(employeeForUpdate);
        doReturn(employeeForUpdate).when(employeeRepo).save(employeeForUpdate);
        EmployeeResponseDTO employeeResponseDTOForTest = employeeService.update(employeeUpdateDTO, 1L);
        assertThat(employeeResponseDTOForTest.title()).isEqualTo(employeeUpdateDTO.title());
        assertThat(employeeResponseDTOForTest.ratePerHour()).isEqualTo(employeeUpdateDTO.ratePerHour());
    }
}
