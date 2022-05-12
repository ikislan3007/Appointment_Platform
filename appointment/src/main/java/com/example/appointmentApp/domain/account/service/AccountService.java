package com.example.appointmentApp.domain.account.service;

import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.account.models.AccountUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AccountService {
    AccountResponseDTO save(AccountCreateDTO account);

    AccountResponseDTO get(Long id);

    Page<AccountResponseDTO> getAll(Pageable pageable);

    AccountResponseDTO update(AccountUpdateDTO account, Long id);

    Long delete(Long id);

    AccountResponseDTO updateAccountRole(Long accountId, Long roleId);

}


