package com.example.appointmentApp.domain.employee.repository;

import com.example.appointmentApp.domain.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    List<Employee> findByProviderId(Long providerId);
    Employee findByAccountEmail(String email);
}
