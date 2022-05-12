package com.example.appointmentApp.infrastructure.security;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MyAccountDetailsService implements UserDetailsService {

    private AccountRepository accountRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account.getRole().getRoleName().equals("admin")) {
            return new User(account.getEmail(), passwordEncoder.encode(account.getPassword()), Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
        }
        else if (account.getRole().getRoleName().equals("user")) {
            return new User(account.getEmail(), passwordEncoder.encode(account.getPassword()), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }
        else if (account.getRole().getRoleName().equals("PROVIDER_ADMIN")) {
            return new User(account.getEmail(), passwordEncoder.encode(account.getPassword()), Collections.singletonList(new SimpleGrantedAuthority("ROLE_PROVIDER_ADMIN")));
        }
        else if (account.getRole().getRoleName().equals("CLIENT")) {
            return new User(account.getEmail(), passwordEncoder.encode(account.getPassword()), Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT")));
        }
        else if (account.getRole().getRoleName().equals("EMPLOYEE")) {
            return new User(account.getEmail(), passwordEncoder.encode(account.getPassword()), Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE")));
        }
        else throw new UsernameNotFoundException(String.format("User with the specified username: %s is not found", email));
    }


    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}