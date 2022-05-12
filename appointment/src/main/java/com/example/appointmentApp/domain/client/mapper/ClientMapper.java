package com.example.appointmentApp.domain.client.mapper;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.client.entity.Client;
import com.example.appointmentApp.domain.client.models.ClientCreateDTO;
import com.example.appointmentApp.domain.client.models.ClientResponseDTO;
import com.example.appointmentApp.domain.client.models.ClientUpdateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client map(ClientCreateDTO clientCreateDTO);
    Account map(AccountCreateDTO accountCreateDTO);
    Client map(ClientUpdateDTO clientUpdateDTO);
    ClientResponseDTO map(Client client);
    AccountResponseDTO map(Account role);
}
