package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.account.models.AccountRoleAssignDTO;
import com.example.appointmentApp.domain.account.models.AccountUpdateDTO;
import com.example.appointmentApp.domain.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/account")
public class AccountController {
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountCreateDTO account) {
        return ResponseEntity.ok(accountService.save(account));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AccountResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.get(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<AccountResponseDTO>> get(Pageable pageable) {
        return ResponseEntity.ok(accountService.getAll(pageable));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AccountUpdateDTO task) {
        AccountResponseDTO accountResponseDTO = accountService.update(task, id);
        return ResponseEntity.ok(accountResponseDTO);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/role")
    public ResponseEntity<AccountResponseDTO> addRoleToUser(@Valid@RequestBody AccountRoleAssignDTO accountRoleAssignDTO) {
        AccountResponseDTO accountResponseDTO = accountService.updateAccountRole(accountRoleAssignDTO.accountId(), accountRoleAssignDTO.roleId());
        return ResponseEntity.ok(accountResponseDTO);
    }

    @Autowired
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

}

