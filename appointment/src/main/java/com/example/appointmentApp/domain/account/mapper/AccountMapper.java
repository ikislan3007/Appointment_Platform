package com.example.appointmentApp.domain.account.mapper;

import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.account.models.AccountUpdateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account map(AccountCreateDTO accountCreateDTO);
    Account map(AccountUpdateDTO accountCreateDTO);
    RoleResponseDTO map(Role role);
    AccountResponseDTO map(Account account);
}
