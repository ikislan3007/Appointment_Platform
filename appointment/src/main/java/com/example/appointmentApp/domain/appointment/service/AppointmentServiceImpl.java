package com.example.appointmentApp.domain.appointment.service;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import com.example.appointmentApp.domain.activity.repository.ActivityRepository;
import com.example.appointmentApp.domain.appointment.entity.Appointment;
import com.example.appointmentApp.domain.appointment.mapper.AppointmentMapper;
import com.example.appointmentApp.domain.appointment.models.AppointmentCreateDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentResponseDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentUpdateDTO;
import com.example.appointmentApp.domain.appointment.repository.AppointmentRepository;
import com.example.appointmentApp.domain.client.entity.Client;
import com.example.appointmentApp.domain.client.repository.ClientRepository;
import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.employee.repository.EmployeeRepository;
import com.example.appointmentApp.infrastructure.custom.appointment.AppointmentNotFoundException;
import com.example.appointmentApp.infrastructure.custom.appointment.NoAccessToAppointmentException;
import com.example.appointmentApp.infrastructure.custom.employee.NoAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl extends AppointmentServiceBase implements AppointmentService {
    private AppointmentRepository appointmentRepo;
    private AppointmentMapper appointmentMapper;
    private ClientRepository clientRepo;
    private AccountRepository accountRepo;
    private EmployeeRepository employeeRepo;
    private ActivityRepository activityRepository;
    private static final String PROVIDER_ADMIN = "PROVIDER_ADMIN";
    private static final String CLIENT = "CLIENT";
    private static final String EMPLOYEE = "EMPLOYEE";
    @Override
    public AppointmentResponseDTO saveWithRoleProviderAdmin(AppointmentCreateDTO appointmentCreateDTO) {
        Appointment appointmentWithDurationAndPrice = addDurationAndPriceToAppointment(
                PROVIDER_ADMIN,
                appointmentCreateDTO.employeeId(),
                appointmentCreateDTO.clientId(),
                appointmentMapper.map(appointmentCreateDTO),
                appointmentCreateDTO.activitiesId()
        );

        Appointment savedAppointment = appointmentRepo.save(appointmentWithDurationAndPrice);
        return appointmentMapper.map(savedAppointment);
    }

    @Override
    public AppointmentResponseDTO updateWithRoleProviderAdmin(AppointmentUpdateDTO appointmentUpdateDTO, Long appointmentId) {
        Appointment appointmentForUpdate = appointmentRepo.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Appointment appointmentWithDurationAndPrice = addDurationAndPriceToAppointment(
                PROVIDER_ADMIN,
                appointmentUpdateDTO.employeeId(),
                appointmentUpdateDTO.clientId(),
                appointmentForUpdate,
                appointmentUpdateDTO.activitiesId()
        );

        Appointment appointmentForSave = appointmentRepo.save(appointmentWithDurationAndPrice);
        appointmentMapper.update(appointmentForSave, appointmentUpdateDTO);
        return appointmentMapper.map(appointmentRepo.save(appointmentForSave));
    }

    @Override
    public AppointmentResponseDTO saveWithRoleEmployee(AppointmentCreateDTO appointmentCreateDTO) {
        Appointment appointmentWithDurationAndPrice = addDurationAndPriceToAppointment(
                EMPLOYEE,
                appointmentCreateDTO.employeeId(),
                appointmentCreateDTO.clientId(),
                appointmentMapper.map(appointmentCreateDTO),
                appointmentCreateDTO.activitiesId()
        );
        Appointment savedAppointment = appointmentRepo.save(appointmentWithDurationAndPrice);
        return appointmentMapper.map(savedAppointment);

    }

    @Override
    public AppointmentResponseDTO updateWithRoleEmployee(AppointmentUpdateDTO appointmentUpdateDTO, Long appointmentId) {
        Appointment appointmentForUpdate = appointmentRepo.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Appointment appointmentWithDurationAndPrice = addDurationAndPriceToAppointment(
                EMPLOYEE,
                appointmentUpdateDTO.employeeId(),
                appointmentUpdateDTO.clientId(),
                appointmentForUpdate,
                appointmentUpdateDTO.activitiesId()
        );

        Appointment appointmentForSave = appointmentRepo.save(appointmentWithDurationAndPrice);
        appointmentMapper.update(appointmentForSave, appointmentUpdateDTO);
        return appointmentMapper.map(appointmentRepo.save(appointmentForSave));
    }

    @Override
    public AppointmentResponseDTO saveWithRoleClient(AppointmentCreateDTO appointmentCreateDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client currentClient = clientRepo.findByAccountEmail(auth.getName());
        Appointment appointmentWithDurationAndPrice = addDurationAndPriceToAppointment(
                CLIENT,
                appointmentCreateDTO.employeeId(),
                currentClient.getId(),
                appointmentMapper.map(appointmentCreateDTO),
                appointmentCreateDTO.activitiesId()
        );
        Appointment savedAppointment = appointmentRepo.save(appointmentWithDurationAndPrice);
        return appointmentMapper.map(savedAppointment);
    }

    @Override
    public AppointmentResponseDTO updateWithRoleClient(AppointmentUpdateDTO appointmentUpdateDTO, Long appointmentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client currentClient = clientRepo.findByAccountEmail(auth.getName());
        Appointment appointmentForUpdate = appointmentRepo.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException(appointmentId));

        Appointment appointmentWithDurationAndPrice = addDurationAndPriceToAppointment(
                CLIENT,
                appointmentUpdateDTO.employeeId(),
                currentClient.getId(),
                appointmentForUpdate,
                appointmentUpdateDTO.activitiesId()
        );

        Appointment appointmentForSave = appointmentRepo.save(appointmentWithDurationAndPrice);
        appointmentMapper.update(appointmentForSave, appointmentUpdateDTO);
        return appointmentMapper.map(appointmentRepo.save(appointmentForSave));
    }


    @Override
    public AppointmentResponseDTO get(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account currentAccount = accountRepo.findByEmail(auth.getName());
        Appointment appointment = appointmentRepo.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        Client client = clientRepo.findByAccountEmail(auth.getName());
        Employee currentEmployee = employeeRepo.findByAccountEmail(auth.getName());

        if (currentAccount.getRole().getRoleName().equals(CLIENT) && appointment.getClient().getId().equals(client.getId())) {
            return appointmentMapper.map(appointmentRepo.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id)));
        } else if ((currentAccount.getRole().getRoleName().equals(EMPLOYEE)) && appointment.getEmployee().getId().equals(currentEmployee.getId())) {
            return appointmentMapper.map(appointmentRepo.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id)));
        } else if (currentAccount.getRole().getRoleName().equals(PROVIDER_ADMIN) && (appointment.getEmployee().getId().equals(currentEmployee.getId()) || appointment.getEmployee().getProvider().getId().equals(currentEmployee.getProvider().getId()))) {
            return appointmentMapper.map(appointmentRepo.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id)));
        } else {
            throw new NoAccessToAppointmentException(id);
        }
    }

    @Override
    public Long delete(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account currentAccount = accountRepo.findByEmail(auth.getName());
        Client client = clientRepo.findByAccountEmail(auth.getName());
        Appointment appointment = appointmentRepo.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        Employee currentEmployee = employeeRepo.findByAccountEmail(auth.getName());

        if (currentAccount.getRole().getRoleName().equals(CLIENT) && appointment.getClient().getId().equals(client.getId())) {
        deleteBase(id);
        } else if ((currentAccount.getRole().getRoleName().equals(EMPLOYEE)) && appointment.getEmployee().getId().equals(currentEmployee.getId())) {
            deleteBase(id);
        } else if (currentAccount.getRole().getRoleName().equals(PROVIDER_ADMIN) && (appointment.getEmployee().getId().equals(currentEmployee.getId()) || appointment.getEmployee().getProvider().getId().equals(currentEmployee.getProvider().getId()))) {
            deleteBase(id);
        } else {
            throw new NoAccessException(id);
        }
        return id;
    }

    @Override
    public Page<AppointmentResponseDTO> getAll(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account currentAccount = accountRepo.findByEmail(auth.getName());
        Client client = clientRepo.findByAccountEmail(auth.getName());
        Employee currentEmployee = employeeRepo.findByAccountEmail(auth.getName());

        if (currentAccount.getRole().getRoleName().equals(CLIENT)) {
            return appointmentRepo.findByClientId(client.getId(), pageable).map(appointmentMapper::map);
        } else if (currentAccount.getRole().getRoleName().equals(EMPLOYEE)) {
            return appointmentRepo.findByEmployeeId(currentEmployee.getId(), pageable).map(appointmentMapper::map);
        } else if (currentEmployee.getAccount().getRole().getRoleName().equals(PROVIDER_ADMIN)) {
            return appointmentRepo.findByEmployeeId(currentEmployee.getId(), pageable).map(appointmentMapper::map);
        } else {
            throw new NoAccessToAppointmentException(currentAccount.getId());
        }
    }

    @Autowired
    public void setAppointmentRepo(AppointmentRepository appointmentRepo) {
        this.appointmentRepo = appointmentRepo;
    }

    @Autowired
    public void setAppointmentMapper(AppointmentMapper appointmentMapper) {
        this.appointmentMapper = appointmentMapper;
    }

    @Autowired
    public void setClientRepo(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Autowired
    public void setEmployeeRepo(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Autowired
    public void setAccountRepo(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Autowired
    public void setActivityRepository(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public AccountRepository getAccountRepository() {
        return this.accountRepo;
    }

    @Override
    public ClientRepository getClientRepository() {
        return this.clientRepo;
    }

    @Override
    public EmployeeRepository getEmployeeRepository() {
        return this.employeeRepo;
    }

    @Override
    public ActivityRepository getActivityRepository() {
        return this.activityRepository;
    }
    @Override
    public AppointmentRepository getAppointmentRepository() {
        return this.appointmentRepo;
    }
}
