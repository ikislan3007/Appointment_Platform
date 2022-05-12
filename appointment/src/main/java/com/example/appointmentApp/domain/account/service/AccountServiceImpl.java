package com.example.appointmentApp.domain.account.service;

import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.repository.RoleRepo;
import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.mapper.AccountMapper;
import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.account.models.AccountUpdateDTO;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import com.example.appointmentApp.infrastructure.custom.role.RoleNotFoundException;
import com.example.appointmentApp.infrastructure.custom.account.AccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private AccountRepository accountRepo;
    private AccountMapper accountMapper;
    private RoleRepo roleRepo;

    @Override
    public AccountResponseDTO get(Long id) {
        return accountMapper.map(
                accountRepo.findById(id)
                        .orElseThrow(() -> new AccountNotFoundException(id)));
    }

    @Override
    public Page<AccountResponseDTO> getAll(Pageable pageable) {
        return accountRepo
                .findAll(pageable)
                .map(accountMapper::map);
    }

    @Override
    public AccountResponseDTO save(AccountCreateDTO accountCreateDTO) {
        Account accountForSave = accountMapper.map(accountCreateDTO);
        accountForSave.setRole(roleRepo.findByRoleName("user"));
        Account createdAccount = accountRepo.save(accountForSave);
        return accountMapper.map(createdAccount);
    }

    @Override
    public AccountResponseDTO update(AccountUpdateDTO accountUpdateDTO, Long id) {
        Account accountDb = accountRepo.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        Account accountForUpdate = accountMapper.map(accountUpdateDTO);
        accountForUpdate.setId(accountDb.getId());
        accountForUpdate.setRole(accountDb.getRole());
        return accountMapper.map(accountRepo.save(accountForUpdate));
    }

    @Override
    public Long delete(Long id) {
        if (accountRepo.existsById(id)) {
            accountRepo.deleteById(id);
        } else {
            throw new AccountNotFoundException(id);
        }
        return id;
    }

    @Override
    public AccountResponseDTO updateAccountRole(Long accountId, Long idRole) {
        Account account = accountRepo.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));
        Role role = roleRepo.findById(idRole).orElseThrow(() -> new RoleNotFoundException(idRole));
        account.setRole(role);
        return accountMapper.map(accountRepo.save(account));
    }

    @Autowired
    public void setAccountMapper(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Autowired
    public void setAccountRepo(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Autowired
    public void setRoleRepo(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }


}
