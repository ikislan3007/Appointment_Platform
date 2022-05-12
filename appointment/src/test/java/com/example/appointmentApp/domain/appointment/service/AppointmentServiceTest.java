package com.example.appointmentApp.domain.appointment.service;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import com.example.appointmentApp.domain.activity.entity.Activity;
import com.example.appointmentApp.domain.activity.models.ActivityResponseDTO;
import com.example.appointmentApp.domain.activity.repository.ActivityRepository;
import com.example.appointmentApp.domain.appointment.entity.Appointment;
import com.example.appointmentApp.domain.appointment.mapper.AppointmentMapper;
import com.example.appointmentApp.domain.appointment.models.AppointmentCreateDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentResponseDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentUpdateDTO;
import com.example.appointmentApp.domain.appointment.repository.AppointmentRepository;
import com.example.appointmentApp.domain.client.entity.Client;
import com.example.appointmentApp.domain.client.models.ClientResponseDTO;
import com.example.appointmentApp.domain.client.repository.ClientRepository;
import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.employee.model.EmployeeResponseDTO;
import com.example.appointmentApp.domain.employee.repository.EmployeeRepository;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.infrastructure.custom.appointment.AppointmentNotFoundException;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTest {
    @InjectMocks
    private AppointmentServiceImpl appointmentService;
    @Mock
    private AppointmentMapper appointmentMapper;
    @Mock
    private ClientRepository clientRepo;
    @Mock
    private AppointmentRepository appointmentRepo;
    @Mock
    private AccountRepository accountRepo;
    @Mock
    private EmployeeRepository employeeRepo;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    Authentication auth;
    Account currentAccount;
    Client client;
    Role role;
    Role  roleClient;
    Role roleEmployee;
    Account accountClient;
    Account accountEmployee;
    Employee employee;
    Provider provider;
    Activity activity;
    Set<Activity> activities;
    ClientResponseDTO clientResponseDTO;
    AccountResponseDTO accountResponseDTOClient;
    RoleResponseDTO roleResponseDTOClient;
    EmployeeResponseDTO employeeResponseDTO;
    AccountResponseDTO accountResponseDTOEmployee;
    RoleResponseDTO roleResponseDTOEmployee;
    ActivityResponseDTO activityResponseDTO;
    Appointment appointment;
    AppointmentResponseDTO appointmentResponseDTO;
    Set <ActivityResponseDTO> activityResponseDTOSet;

    @Before
    public void setUp() {
        provider=new Provider( 1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "MONDAY");
        roleResponseDTOClient=new RoleResponseDTO(1L,"CLIENT");
        role=new Role(1L, "PROVIDER_ADMIN");
        roleEmployee=new Role(1l,"EMPLOYEE");
        roleClient=new Role(1L, "CLIENT");
        accountClient=new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",roleClient);
        accountEmployee=new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",roleEmployee);
        currentAccount= new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",role);
        client=new Client(1L,"+1 1234567890123","ul Maria-Luiza 22a",accountClient);
        employee=new Employee(1L,"employeeeee", "+1 1234567810123",4.00,provider,accountEmployee);
        SecurityContextHolder.getContext().setAuthentication(auth);
        Duration duration = Duration.parse("P3DT5H40M30S");
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(employee);
        activity=new Activity(1L,"activity",220,duration, provider,employeeList);
        activities=new HashSet<>();
        activities.add(activity);
        clientResponseDTO=new ClientResponseDTO(1L,"+1 1234567890123","ul Maria-Luiza 22a",accountResponseDTOClient);roleResponseDTOEmployee=new RoleResponseDTO(1l,"EMPLOYEE");
        accountResponseDTOClient=new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",roleResponseDTOClient);
        accountResponseDTOEmployee=new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",roleResponseDTOClient);
        employeeResponseDTO=new EmployeeResponseDTO(1L,"employeeeee", "+1 1234567810123",4.00,provider,accountResponseDTOEmployee);
        activityResponseDTO = new ActivityResponseDTO(1L,"activity",220,duration,provider,employeeList);
        activityResponseDTOSet=new HashSet<>();
        activityResponseDTOSet.add(activityResponseDTO);
        appointment=new Appointment(1l, LocalDateTime.of(2022, 4, 07, 0, 0,0),LocalDateTime.of(2022, 5, 07, 00, 00,00), client,employee,activities,900.00);
        appointmentResponseDTO=new AppointmentResponseDTO(1l, LocalDateTime.of(2022, 4, 07, 00, 00,00),LocalDateTime.of(2022, 5, 07, 00, 00,00), clientResponseDTO,employeeResponseDTO,activityResponseDTOSet,900.00);
    }

    @Test
    public void createAppointmentTest() {
        Mockito.when(accountRepo.findByEmail(auth.getName())).thenReturn(currentAccount);
        Appointment appointmentForCreation=new Appointment(1l, LocalDateTime.of(2022, 4, 07, 00, 00,00),LocalDateTime.of(2022, 5, 07, 00, 00,00),
                client,employee,activities,900.00);
        List<Long> activitiesId=new ArrayList<>();
        activitiesId.add(1L);
        AppointmentCreateDTO appointmentCreateDTO=new AppointmentCreateDTO( LocalDateTime.of(2022, 4, 07, 00, 00,00),1L,activitiesId, 1L);
        Mockito.when(employeeRepo.findById(any())).thenReturn(Optional.of(employee));
        doReturn(employee).when(employeeRepo).findByAccountEmail(any());
        Mockito.when(clientRepo.findById(any())).thenReturn(Optional.of(client));
        Mockito.when(activityRepository.findById(any())).thenReturn(Optional.of(activity));
        AppointmentResponseDTO appointmentResponseDTO=new AppointmentResponseDTO(1l, LocalDateTime.of(2022, 4, 07, 00, 00,00),LocalDateTime.of(2022, 5, 07, 00, 00,00), clientResponseDTO,employeeResponseDTO,activityResponseDTOSet,900.00);
        doReturn(appointmentForCreation).when(appointmentMapper).map(appointmentCreateDTO);
        doReturn(appointment).when(appointmentRepo).save(any());
        doReturn(appointmentResponseDTO).when(appointmentMapper).map(appointment);

        AppointmentResponseDTO appointmentResponseDTOForTest = appointmentService.saveWithRoleProviderAdmin(appointmentCreateDTO);
        assertThat(appointmentResponseDTOForTest.startDate()).isEqualTo(appointmentCreateDTO.startDate());
        assertThat(appointmentResponseDTOForTest.client().id()).isEqualTo(appointmentCreateDTO.clientId());
        assertThat(appointmentResponseDTOForTest.employee().id()).isEqualTo(appointmentCreateDTO.employeeId());
    }
    @Test
    public void updateAppointmentTest() {
        Mockito.when(accountRepo.findByEmail(auth.getName())).thenReturn(currentAccount);
        Mockito.when(appointmentRepo.findById(any())).thenReturn(Optional.of(appointment));
        List<Long> activitiesId=new ArrayList<>();
        activitiesId.add(1L);
        AppointmentUpdateDTO appointmentUpdateDTO=new AppointmentUpdateDTO( LocalDateTime.of(2022, 4, 07, 00, 00,00),1L,activitiesId, 1L);
        Mockito.when(employeeRepo.findById(any())).thenReturn(Optional.of(employee));
        doReturn(employee).when(employeeRepo).findByAccountEmail(any());
        Mockito.when(clientRepo.findById(any())).thenReturn(Optional.of(client));
        Mockito.when(activityRepository.findById(any())).thenReturn(Optional.of(activity));
        AppointmentResponseDTO appointmentResponseDTO=new AppointmentResponseDTO(1l, LocalDateTime.of(2022, 4, 07, 00, 00,00),LocalDateTime.of(2022, 5, 07, 00, 00,00), clientResponseDTO,employeeResponseDTO,activityResponseDTOSet,900.00);
        doNothing().when(appointmentMapper).update(appointment,appointmentUpdateDTO);
        doReturn(appointment).when(appointmentRepo).save(any());
        doReturn(appointmentResponseDTO).when(appointmentMapper).map(appointment);

        AppointmentResponseDTO appointmentResponseDTOForTest = appointmentService.updateWithRoleProviderAdmin(appointmentUpdateDTO,1L);
        assertThat(appointmentResponseDTOForTest.startDate()).isEqualTo(appointmentUpdateDTO.startDate());
        assertThat(appointmentResponseDTOForTest.client().id()).isEqualTo(appointmentUpdateDTO.clientId());
        assertThat(appointmentResponseDTOForTest.employee().id()).isEqualTo(appointmentUpdateDTO.employeeId());
    }

    @Test
    public void getAppointmentByIdTest() {
        Mockito.when(accountRepo.findByEmail(auth.getName())).thenReturn(currentAccount);
        Mockito.when(employeeRepo.findByAccountEmail(auth.getName())).thenReturn(employee);
        doReturn(Optional.of(appointment)).when(appointmentRepo).findById(appointment.getId());
        doReturn(appointmentResponseDTO).when(appointmentMapper).map(appointment);

        AppointmentResponseDTO appointmentResponseDTOFromService = this.appointmentService.get(1L);
        assertThat(appointmentResponseDTOFromService.id()).isEqualTo(appointment.getId());
    }

    @Test
    public void getAppointmentByIdNotExistingIdShouldReturnAppointmentNotFoundException() {
        Mockito.when(appointmentRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> appointmentService.get(1L));
    }

    @Test
    public void findAllAppointments() {
        int pageNumber = 0;
        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Appointment> appointmentsPage = new PageImpl<>(Collections.singletonList(appointment));
        when(appointmentRepo.findAll(pageable)).thenReturn(appointmentsPage);
        Page<Appointment> appointments = appointmentRepo.findAll(pageable);
        assertEquals(1, appointments.getNumberOfElements());
    }

    @Test
    public void deleteAppointmentTest() {
        Mockito.when(accountRepo.findByEmail(auth.getName())).thenReturn(currentAccount);
        Mockito.when(appointmentRepo.findById(1L)).thenReturn(Optional.ofNullable(appointment));
        Employee currentEmployee= new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  accountEmployee);
        Mockito.when(employeeRepo.findByAccountEmail(auth.getName())).thenReturn(currentEmployee);
        Mockito.when(appointmentRepo.existsById(appointment.getId())).thenReturn(true);
        appointmentService.delete(appointment.getId());
        assertThat(appointmentService.delete(activity.getId())).isEqualTo(activity.getId());
    }
    @Test
    public void deleteActivityShouldReturnActivityNotFoundException() {Mockito.when(appointmentRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(AppointmentNotFoundException.class, () -> appointmentService.delete(1L));
    }

}
