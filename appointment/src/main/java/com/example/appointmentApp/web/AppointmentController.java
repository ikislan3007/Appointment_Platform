package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import com.example.appointmentApp.domain.appointment.models.AppointmentCreateDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentResponseDTO;
import com.example.appointmentApp.domain.appointment.models.AppointmentUpdateDTO;
import com.example.appointmentApp.domain.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private AppointmentService appointmentService;
    private AccountRepository accountRepo;

    @PreAuthorize("hasRole('PROVIDER_ADMIN')"+"||hasRole('EMPLOYEE')"+"||hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<AppointmentResponseDTO> create(@Valid @RequestBody AppointmentCreateDTO appointmentCreateDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account currentAccount = accountRepo.findByEmail(auth.getName());
        if (currentAccount.getRole().getRoleName().equals("PROVIDER_ADMIN")){
            return ResponseEntity.ok(appointmentService.saveWithRoleProviderAdmin(appointmentCreateDTO));
        }else if(currentAccount.getRole().getRoleName().equals("EMPLOYEE")){
            return ResponseEntity.ok(appointmentService.saveWithRoleEmployee(appointmentCreateDTO));
        }else{
            return ResponseEntity.ok(appointmentService.saveWithRoleClient(appointmentCreateDTO));
        }
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')"+"||hasRole('EMPLOYEE')"+"||hasRole('CLIENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.delete(id));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')"+"||hasRole('EMPLOYEE')"+"||hasRole('CLIENT')")
    @GetMapping
    public ResponseEntity<Page<AppointmentResponseDTO>> get(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER_ADMIN')"+"||hasRole('EMPLOYEE')"+"||hasRole('CLIENT')")
    public ResponseEntity<AppointmentResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.get(id));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')"+"||hasRole('EMPLOYEE')"+"||hasRole('CLIENT')")
    @PatchMapping("/{id}")
    public ResponseEntity<AppointmentResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AppointmentUpdateDTO appointmentUpdateDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account currentAccount = accountRepo.findByEmail(auth.getName());
        if (currentAccount.getRole().getRoleName().equals("PROVIDER_ADMIN")){
            return ResponseEntity.ok(appointmentService.updateWithRoleProviderAdmin(appointmentUpdateDTO,id));
        }else if(currentAccount.getRole().getRoleName().equals("EMPLOYEE")){
            return ResponseEntity.ok(appointmentService.updateWithRoleEmployee(appointmentUpdateDTO,id));
        }else{
            return ResponseEntity.ok(appointmentService.updateWithRoleClient(appointmentUpdateDTO,id));
        }
    }
    @Autowired
    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @Autowired
    public void setAccountRepo(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

}




