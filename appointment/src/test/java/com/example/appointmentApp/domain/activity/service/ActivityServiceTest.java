package com.example.appointmentApp.domain.activity.service;

import com.example.appointmentApp.domain.account.entity.Account;
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
import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.infrastructure.custom.activity.ActivityNotFoundException;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceTest {
    @InjectMocks
    private ActivityServiceImpl activityService;
    @Mock
    private ActivityMapper activityMapper;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private EmployeeRepository employeeRepo;
    @Mock
    private ProviderRepository providerRepository;
    @Mock
    Authentication auth;
    Activity activity;
    Provider provider;
    Account account;
    Role role;
    Employee employee;
    ActivityResponseDTO activityResponseDTO;

    @Before
    public void setUp() {
        role = new Role(1L, "PROVIDER_ADMIN");
        SecurityContextHolder.getContext().setAuthentication(auth);
        account = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",role);
        provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        employee=new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        account = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",role);
        Duration duration = Duration.parse("P3DT5H40M30S");
        provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(employee);
        activity=new Activity(1L,"activity",220,duration, provider,employeeList);
        activityResponseDTO = new ActivityResponseDTO(1L,"activity",220,duration,provider,employeeList);
    }

    @Test
    public void createActivityTest() {
        Employee currentProvider=new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        doReturn(currentProvider).when(employeeRepo).findByAccountEmail(any());
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(employee);
        doReturn(Optional.of(employee)).when(employeeRepo).findById(any());
        doReturn(provider).when(providerRepository).findByName(any());
        Duration duration = Duration.parse("P3DT5H40M30S");
        Activity activityForCreation=new Activity(1L,"activity",220,duration, provider,employeeList);
        List<Long> employeesId=new ArrayList<>();
        employeesId.add(1L);
        ActivityCreateDTO activityCreateDTO = new ActivityCreateDTO("activity",220,duration, "provider",employeesId);
        doReturn(activityForCreation).when(activityMapper).map(activityCreateDTO);
        doReturn(activity).when(activityRepository).save(any());
        doReturn(activityResponseDTO).when(activityMapper).map(activity);
        ActivityResponseDTO activityResponseDTOForTest = activityService.save(activityCreateDTO);

        assertThat(activityResponseDTOForTest.name()).isEqualTo(activityCreateDTO.name());
        assertThat(activityResponseDTOForTest.price()).isEqualTo(activityCreateDTO.price());
        assertThat(activityResponseDTOForTest.duration()).isEqualTo(activityCreateDTO.duration());
    }
    @Test
    public void updateActivityTest() {

        Employee currentProvider=new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        doReturn(currentProvider).when(employeeRepo).findByAccountEmail(any());
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(employee);
        doReturn(Optional.of(employee)).when(employeeRepo).findById(any());
        Employee currentEmployee= new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        Mockito.when(employeeRepo.findByAccountEmail(auth.getName())).thenReturn(currentEmployee);
        Duration duration = Duration.parse("P3DT5H40M30S");
        List<Long> employeesId=new ArrayList<>();
        employeesId.add(1L);
        ActivityUpdateDTO activityUpdateDTO = new ActivityUpdateDTO("activity",220,duration,employeesId);
        doReturn(Optional.of(activity)).when(activityRepository).findById(1l);
        doNothing().when(activityMapper).update(activity,activityUpdateDTO);
        doReturn(activity).when(activityRepository).save(any());
        doReturn(activityResponseDTO).when(activityMapper).map(activity);
        ActivityResponseDTO activityResponseDTOForTest = activityService.update(activityUpdateDTO, 1L);
        assertThat(activityResponseDTOForTest.name()).isEqualTo(activityUpdateDTO.name());
        assertThat(activityResponseDTOForTest.price()).isEqualTo(activityUpdateDTO.price());
        assertThat(activityResponseDTOForTest.duration()).isEqualTo(activityUpdateDTO.duration());
    }

    @Test
    public void getActivityByIdTest() {
        Employee currentEmployee= new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        Mockito.when(employeeRepo.findByAccountEmail(auth.getName())).thenReturn(currentEmployee);
        doReturn(Optional.of(activity)).when(activityRepository).findById(activity.getId());
        doReturn(activityResponseDTO).when(activityMapper).map(activity);
        ActivityResponseDTO activityResponseDTOFromService = this.activityService.get(1L);
        assertThat(activityResponseDTOFromService.id()).isEqualTo(activity.getId());
    }

    @Test
    public void getActivityByIdNotExistingIdShouldReturnActivityNotFoundException() {
        Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ActivityNotFoundException.class, () -> activityService.get(1L));
    }
    @Test
    public void findAllActivities() {
        int pageNumber = 0;
        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Activity> activityPage = new PageImpl<>(Collections.singletonList(activity));
        when(activityRepository.findAll(pageable)).thenReturn(activityPage);
        Page<Activity> activities = activityRepository.findAll(pageable);
        assertEquals(1, activities.getNumberOfElements());
    }
    @Test
    public void deleteActivityTest() {
        Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.ofNullable(activity));
        Employee currentEmployee= new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        Mockito.when(employeeRepo.findByAccountEmail(auth.getName())).thenReturn(currentEmployee);
        Mockito.when(activityRepository.existsById(activity.getId())).thenReturn(true);
        doNothing().when(activityRepository).delete(activity);
        activityService.delete(activity.getId());
        assertThat(activityService.delete(activity.getId())).isEqualTo(activity.getId());
    }
    @Test
    public void deleteActivityShouldReturnActivityNotFoundException() {Mockito.when(activityRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ActivityNotFoundException.class, () -> activityService.delete(1L));
    }
}
