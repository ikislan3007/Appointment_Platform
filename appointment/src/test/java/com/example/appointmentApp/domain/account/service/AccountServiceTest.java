package com.example.appointmentApp.domain.account.service;

import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.domain.role.repository.RoleRepo;
import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.mapper.AccountMapper;
import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.account.models.AccountUpdateDTO;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import com.example.appointmentApp.infrastructure.custom.role.RoleNotFoundException;
import com.example.appointmentApp.infrastructure.custom.account.AccountNotFoundException;
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
import java.util.Collections;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountServiceTest {
    @InjectMocks
    private AccountServiceImpl accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private RoleRepo roleRepo;
    @Mock
    Account account;
    RoleResponseDTO roleResponseDTO;
    Role role;


    @Before
    public void setUp() {
        account = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan");
        roleResponseDTO = new RoleResponseDTO(1L, "Admin");

        role = new Role(1L, "account");
    }

    @Test
    public void getAccountByIdTest() {
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        doReturn(Optional.of(account)).when(accountRepository).findById(account.getId());
        doReturn(accountResponseDTO).when(accountMapper).map(account);
        AccountResponseDTO accountResponseDTOFromService = this.accountService.get(1L);
        assertThat(accountResponseDTOFromService.id()).isEqualTo(account.getId());
    }

    @Test
    public void getAccountByIdShouldReturnAccountNotFoundException() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(AccountNotFoundException.class, () -> accountService.get(1L));
    }

    @Test
    public void deleteAccountTest() {
        Mockito.when(accountRepository.existsById(account.getId())).thenReturn(true);
        accountService.delete(account.getId());
        assertThat(accountService.delete(account.getId())).isEqualTo(account.getId());
    }

    @Test
    public void deleteAccountShouldReturnAccountNotFoundException() {
        Mockito.when(accountRepository.existsById(account.getId())).thenReturn(false);
        Assertions.assertThrows(AccountNotFoundException.class, () -> accountService.delete(1L));

    }

    @Test
    public void createAccountTest() {
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO("Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", role);
        doReturn(account).when(accountMapper).map(accountCreateDTO);
        Account createdAccount = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan");
        doReturn(createdAccount).when(accountRepository).save(account);
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        doReturn(accountResponseDTO).when(accountMapper).map(createdAccount);
        AccountResponseDTO accountResponseDTOForTest = accountService.save(accountCreateDTO);

        assertThat(accountResponseDTOForTest.email()).isEqualTo(accountCreateDTO.email());
        assertThat(accountResponseDTOForTest.fullName()).isEqualTo(accountCreateDTO.fullName());
        assertThat(accountResponseDTOForTest.password()).isEqualTo(accountCreateDTO.password());
    }

    @Test
    public void updateAccountTest() {
        AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO("Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        Account accountForUpdate = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan");
        doReturn(Optional.of(account)).when(accountRepository).findById(1L);
        doReturn(accountForUpdate).when(accountMapper).map(accountUpdateDTO);
        doReturn(accountResponseDTO).when(accountMapper).map(accountForUpdate);
        doReturn(accountForUpdate).when(accountRepository).save(accountForUpdate);
        AccountResponseDTO accountResponseDTOForTest = accountService.update(accountUpdateDTO, 1L);
        assertThat(accountResponseDTOForTest.email()).isEqualTo(accountResponseDTO.email());
        assertThat(accountResponseDTOForTest.fullName()).isEqualTo(accountResponseDTO.fullName());
        assertThat(accountResponseDTOForTest.password()).isEqualTo(accountResponseDTO.password());
    }

    @Test
    public void updateAccountByNotExistingIdShouldReturnAccountNotFoundException() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(AccountNotFoundException.class, () -> accountService.get(1L));
    }

    @Test
    public void findAllAccounts() {
        int pageNumber = 0;
        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Account> accountPage = new PageImpl<>(Collections.singletonList(account));
        when(accountRepository.findAll(pageable)).thenReturn(accountPage);
        Page<Account> accounts = accountRepository.findAll(pageable);
        assertEquals(1, accounts.getNumberOfElements());
    }



    @Test
    public void updateAccountRole() {
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(roleRepo.findById(role.getId())).thenReturn(Optional.of(role));
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        Account savedAccount = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", role);
        doReturn(accountResponseDTO).when(accountMapper).map(savedAccount);
        doReturn(savedAccount).when(accountRepository).save(account);
        AccountResponseDTO accountResponseDTOForTest = accountService.updateAccountRole(accountResponseDTO.id(), accountResponseDTO.role().id());

        assertThat(accountResponseDTOForTest.email()).isEqualTo(accountResponseDTO.email());
        assertThat(accountResponseDTOForTest.fullName()).isEqualTo(accountResponseDTO.fullName());
        assertThat(accountResponseDTOForTest.password()).isEqualTo(accountResponseDTO.password());
        assertThat(accountResponseDTOForTest.role()).isEqualTo(accountResponseDTO.role());

    }
    @Test
    public void updateAccountRoleShouldReturnAccountNotFoundException() {
        Mockito.when(accountRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(AccountNotFoundException.class, () -> accountService.updateAccountRole(1L,1l));

    }
    @Test
    public void updateAccountRoleShouldReturnRoleNotFoundException() {
        doReturn(Optional.of(account)).when(accountRepository).findById(account.getId());
        Mockito.when(roleRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RoleNotFoundException.class, () -> accountService.updateAccountRole(1L,1l));

    }

}




