package com.example.appointmentApp.domain.appointment.service;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import com.example.appointmentApp.domain.activity.entity.Activity;
import com.example.appointmentApp.domain.activity.repository.ActivityRepository;
import com.example.appointmentApp.domain.appointment.entity.Appointment;
import com.example.appointmentApp.domain.appointment.repository.AppointmentRepository;
import com.example.appointmentApp.domain.client.repository.ClientRepository;
import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.employee.repository.EmployeeRepository;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.infrastructure.custom.activity.ActivityNotFoundException;
import com.example.appointmentApp.infrastructure.custom.appointment.EmployeeCanNotAssignAnotherEmployeeException;
import com.example.appointmentApp.infrastructure.custom.appointment.NoAccessToAppointmentException;
import com.example.appointmentApp.infrastructure.custom.appointment.ProviderIsNotSameForEmployeeAndActivities;
import com.example.appointmentApp.infrastructure.custom.client.ClientNotFoundException;
import com.example.appointmentApp.infrastructure.custom.employee.EmployeeNotFoundException;
import com.example.appointmentApp.infrastructure.custom.employee.NoAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AppointmentServiceBase {
    public abstract AccountRepository getAccountRepository();
    public abstract ClientRepository getClientRepository();
    public abstract EmployeeRepository getEmployeeRepository();
    public abstract ActivityRepository getActivityRepository();
    public abstract AppointmentRepository getAppointmentRepository();
    public Appointment addDurationAndPriceToAppointment(String roleName, Long employeeId, Long clientId, Appointment appointment, List<Long> activityIds) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account currentAccount = getAccountRepository().findByEmail(auth.getName());

        if (currentAccount.getRole().getRoleName().equals(roleName)) {
            Employee employeeForAppointment = getEmployeeRepository().findById(employeeId).orElseThrow(() -> new EmployeeNotFoundException(employeeId));
            Employee currentEmployee=getEmployeeRepository().findByAccountEmail(currentAccount.getEmail());
            if (!(employeeForAppointment.getProvider().getName().equals(currentEmployee.getProvider().getName()))) {
                throw new NoAccessException(employeeId);
            } else {
                appointment.setEmployee(employeeForAppointment);
            }
            appointment.setClient(getClientRepository().findById(clientId).orElseThrow(() -> new ClientNotFoundException(clientId)));
            Set<Activity> activitiesList = new HashSet<>();
            activityIds.forEach(id -> activitiesList.add(getActivityRepository().findById(id)
                    .orElseThrow(() -> new ActivityNotFoundException(id))));
            for (Activity activity : activitiesList) {
                if (currentAccount.getRole().getRoleName().equals("PROVIDER_ADMIN") &&  (!(employeeForAppointment.getAccount().getId().equals(currentAccount.getId())))){
                    throw new ProviderIsNotSameForEmployeeAndActivities(employeeForAppointment.getProvider().getId());
                }

                if (currentAccount.getRole().getRoleName().equals("EMPLOYEE") && !(employeeForAppointment.getAccount().getId().equals(currentAccount.getId()))) {
                    throw new EmployeeCanNotAssignAnotherEmployeeException(currentAccount.getId(), employeeForAppointment.getId());
                }

                if (currentAccount.getRole().getRoleName().equals("CLIENT") && !(employeeForAppointment.getProvider().getName().equals(activity.getProvider().getName()))) {
                    throw new ProviderIsNotSameForEmployeeAndActivities(employeeForAppointment.getProvider().getId());
                }
            }
            Appointment appointmentWithDurationAndPrice = calculateDurationAndPriceForActivities(appointment, activitiesList);
            LocalDateTime endDate = this.calculateEndDate(appointmentWithDurationAndPrice.getActivities(), appointmentWithDurationAndPrice, employeeForAppointment.getProvider());
            appointmentWithDurationAndPrice.setEndDate(endDate);
            return appointmentWithDurationAndPrice;
        } else {
            throw new NoAccessToAppointmentException(currentAccount.getId());
        }
    }

    public void deleteBase(Long id){
        if (getAppointmentRepository().existsById(id)) {
            getAppointmentRepository().deleteById(id);
        } else {
            throw new ActivityNotFoundException(id);
        }
    }

    private LocalDateTime calculateEndDate(Set<Activity> activities, Appointment appointment, Provider provider) {
        Duration totalHours = Duration.ZERO;
        for (Activity activity : activities) {
            totalHours = totalHours.plus(activity.getDuration());
        }
        Duration hoursBetween = Duration.between(provider.getStartOfTheWorkingDay(), provider.getEndOfTheWorkingDay());
        LocalDateTime nextBusinessDay = appointment.getStartDate();
        totalHours = totalHours.minus(hoursBetween);
        while (!totalHours.isNegative()) {
            nextBusinessDay = this.getNextBusinessDay(nextBusinessDay, provider.getWorkingDays());
            totalHours = totalHours.minus(hoursBetween);
        }
        return nextBusinessDay;
    }

    public LocalDateTime getNextBusinessDay(LocalDateTime currentBusinessDate, String workingDays) {
        LocalDateTime nextBusinessDay = currentBusinessDate.plusDays(1);
        while (!workingDays.contains(nextBusinessDay.getDayOfWeek().name())) {
            nextBusinessDay = nextBusinessDay.plusDays(1);
        }
        return nextBusinessDay;
    }

    public Appointment calculateDurationAndPriceForActivities(Appointment appointment, Set<Activity> activities) {
        double priceSum = 0;
        Duration durationSum = Duration.ZERO;
        for (Activity activity : activities) {
            priceSum += activity.getPrice();
            durationSum = durationSum.plus(activity.getDuration());
        }
        LocalDateTime endDay = appointment.getStartDate().plus(durationSum);
        appointment.setPrice(priceSum);
        appointment.setEndDate(endDay);
        appointment.setActivities(activities);

        return appointment;
    }
}
