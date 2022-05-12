package com.example.appointmentApp.domain.client.service;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import com.example.appointmentApp.domain.client.entity.Client;
import com.example.appointmentApp.domain.client.mapper.ClientMapper;
import com.example.appointmentApp.domain.client.models.ClientCreateDTO;
import com.example.appointmentApp.domain.client.models.ClientResponseDTO;
import com.example.appointmentApp.domain.client.models.ClientUpdateDTO;
import com.example.appointmentApp.domain.client.repository.ClientRepository;
import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.domain.role.repository.RoleRepo;
import com.example.appointmentApp.infrastructure.custom.client.ClientNotFoundException;
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
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {
    @InjectMocks
    private ClientServiceImpl clientServices;
    @Mock
    private ClientRepository clientRepo;
    @Mock
    private ClientMapper clientMapper;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    private AccountRepository accountRepo;
    @Mock
    Authentication auth;
    AccountResponseDTO accountResponseDTO;
    RoleResponseDTO roleResponseDTO;
    Client client;
    Account account;
    Role role;

    @Before
    public void setUp() {
        SecurityContextHolder.getContext().setAuthentication(auth);
        roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",roleResponseDTO);
        account = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan");
        role = new Role(1L, "account");
        client=new Client(1L,"+1 1234567890123", "Ul.Nikolay Rakitin 23a",  account);
    }

    @Test
    public void getClientByIdTest() {
        Client currentClient= new Client(1L,"+1 1234567890123", "Ul.Nikolay Rakitin 23a",  account);
        Mockito.when(clientRepo.findByAccountEmail(auth.getName())).thenReturn(currentClient);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(1L, "+1 1234567890123", "Ul.Nikolay Rakitin 23a",  accountResponseDTO);
        doReturn(Optional.of(client)).when(clientRepo).findById(client.getId());
        doReturn(clientResponseDTO).when(clientMapper).map(client);
        ClientResponseDTO clientResponseDTOFromService = this.clientServices.get(1L);
        assertThat(clientResponseDTOFromService.id()).isEqualTo(client.getId());
    }

    @Test
    public void getClientByIdShouldReturnClientNotFoundException() {
        Mockito.when(clientRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ClientNotFoundException.class, () -> clientServices.get(1L));
    }

    @Test
    public void deleteClientTest() {
        Mockito.when(clientRepo.findById(1L)).thenReturn(Optional.ofNullable(client));
        Client currentClient= new Client(1L,"+1 1234567890123", "Ul.Nikolay Rakitin 23a",  account);
        Mockito.when(clientRepo.findByAccountEmail(auth.getName())).thenReturn(currentClient);
        Mockito.when(clientRepo.existsById(client.getId())).thenReturn(true);
        clientServices.delete(client.getId());
        assertThat(clientServices.delete(client.getId())).isEqualTo(client.getId());
    }

    @Test
    public void deleteClientShouldReturnClientNotFoundException() {
        Mockito.when(clientRepo.existsById(client.getId())).thenReturn(false);
        Assertions.assertThrows(ClientNotFoundException.class, () -> clientServices.delete(1L));
    }

    @Test
    public void createClientTest() {
        doReturn(role).when(roleRepo).findByRoleName("user");
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO("Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", role);
        doReturn(account).when(clientMapper).map(accountCreateDTO);
        Account createdAccount = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan");
        doReturn(createdAccount).when(accountRepo).save(account);
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);

        ClientCreateDTO clientCreateDTO = new ClientCreateDTO("+1 1234567890123", "Ul.Nikolay Rakitin 23a",  accountCreateDTO);
        doReturn(client).when(clientMapper).map(clientCreateDTO);
        Client createdClient = new Client(1L, "+1 1234567890123", "Ul.Nikolay Rakitin 23a",  account);
        doReturn(createdClient).when(clientRepo).save(client);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(1L, "+1 1234567890123", "Ul.Nikolay Rakitin 23a",  accountResponseDTO);
        doReturn(clientResponseDTO).when(clientMapper).map(createdClient);
        ClientResponseDTO clientResponseDTOForTest = clientServices.save(clientCreateDTO);

        assertThat(clientResponseDTOForTest.phone()).isEqualTo(clientCreateDTO.phone());
        assertThat(clientResponseDTOForTest.address()).isEqualTo(clientCreateDTO.address());
    }

    @Test
    public void updateClientTest() {
        Client currentClient= new Client(1L,"+1 1234567890123", "Ul.Nikolay Rakitin 23a",  account);
        Mockito.when(clientRepo.findByAccountEmail(auth.getName())).thenReturn(currentClient);
        ClientUpdateDTO clientUpdateDTO = new ClientUpdateDTO("+1 1234567890123", "Ul.Nikolay Rakitin 23a");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(1L, "+1 1234567890123", "Ul.Nikolay Rakitin 23a",accountResponseDTO);
        Client clientForUpdate = new Client(1L, "Ivanov", "ivanov.ivan@gmail.com", account);

        doReturn(Optional.of(client)).when(clientRepo).findById(1L);
        doReturn(clientForUpdate).when(clientMapper).map(clientUpdateDTO);
        doReturn(clientResponseDTO).when(clientMapper).map(clientForUpdate);
        doReturn(clientForUpdate).when(clientRepo).save(clientForUpdate);

        ClientResponseDTO clientResponseDTOForTest = clientServices.update(clientUpdateDTO, 1L);
        assertThat(clientResponseDTOForTest.phone()).isEqualTo(clientResponseDTO.phone());
        assertThat(clientResponseDTOForTest.address()).isEqualTo(clientResponseDTO.address());
    }

    @Test
    public void findAllClients() {
        int pageNumber = 0;
        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Client> clientPage = new PageImpl<>(Collections.singletonList(client));
        when(clientRepo.findAll(pageable)).thenReturn(clientPage);
        Page<Client> accounts = clientRepo.findAll(pageable);
        assertEquals(1, accounts.getNumberOfElements());
    }
}
