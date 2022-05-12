package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.employee.model.EmployeeCreateDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeResponseDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeUpdateDTO;
import com.example.appointmentApp.domain.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController{
    private EmployeeService employeeService;

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> create(@Valid @RequestBody EmployeeCreateDTO employeeCreateDTO) {
        return ResponseEntity.ok(employeeService.save(employeeCreateDTO));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.delete(id));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> get(Pageable pageable) {
        return ResponseEntity.ok(employeeService.getAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER_ADMIN')"+"||hasRole('EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.get(id));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')"+"||hasRole('EMPLOYEE')")
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeUpdateDTO employeeUpdateDTO) {
        EmployeeResponseDTO emlResponseDTO = employeeService.update(employeeUpdateDTO, id);
        return ResponseEntity.ok(emlResponseDTO);
    }
    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
}
