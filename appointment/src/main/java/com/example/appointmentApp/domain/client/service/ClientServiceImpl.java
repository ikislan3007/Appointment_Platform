package com.example.appointmentApp.domain.client.service;

import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.account.repository.AccountRepository;
import com.example.appointmentApp.domain.client.entity.Client;
import com.example.appointmentApp.domain.client.mapper.ClientMapper;
import com.example.appointmentApp.domain.client.models.ClientCreateDTO;
import com.example.appointmentApp.domain.client.models.ClientResponseDTO;
import com.example.appointmentApp.domain.client.models.ClientUpdateDTO;
import com.example.appointmentApp.domain.client.repository.ClientRepository;
import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.repository.RoleRepo;
import com.example.appointmentApp.infrastructure.custom.client.ClientNotFoundException;
import com.example.appointmentApp.infrastructure.custom.employee.NoAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    private ClientRepository clientRepo;
    private AccountRepository accountRepo;
    private ClientMapper clientMapper;
    private RoleRepo roleRepo;

    @Override
    public ClientResponseDTO save(ClientCreateDTO clientCreateDTO) {
        Role defaultRoleForUser = roleRepo.findByRoleName("CLIENT");
        Account accountForCreation = accountRepo.save(clientMapper.map(clientCreateDTO.account()));
        accountForCreation.setRole(defaultRoleForUser);
        Client clientForCreation = clientMapper.map(clientCreateDTO);
        clientForCreation.setAccount(accountForCreation);
        Client clientForSave = clientRepo.save(clientForCreation);
        return clientMapper.map(clientForSave);
    }

    @Override
    public ClientResponseDTO get(Long id) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client currentClient = clientRepo.findByAccountEmail(auth.getName());
        if (currentClient.getId() == client.getId()) {
            return clientMapper.map(
                    clientRepo.findById(id)
                            .orElseThrow(() -> new ClientNotFoundException(id)));
        } else {
            throw new NoAccessException(id);
        }
    }

    @Override
    public Page<ClientResponseDTO> getAll(Pageable pageable) {
        return clientRepo
                .findAll(pageable)
                .map(clientMapper::map);
    }

    @Override
    public ClientResponseDTO update(ClientUpdateDTO clientUpdateDTO, Long id) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client currentClient = clientRepo.findByAccountEmail(auth.getName());
        if (currentClient.getId() == client.getId()) {
            Client clientDb = clientRepo.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
            Client clientForUpdate = clientMapper.map(clientUpdateDTO);
            clientForUpdate.setId(clientDb.getId());
            clientForUpdate.setAccount(clientDb.getAccount());
            return clientMapper.map(clientRepo.save(clientForUpdate));
        } else {
            throw new NoAccessException(id);
        }
    }

    @Override
    public Long delete(Long id) {
        Client client = clientRepo.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Client currentClient = clientRepo.findByAccountEmail(auth.getName());
        if (currentClient.getId() == client.getId()) {
            if (clientRepo.existsById(id)) {
                clientRepo.deleteById(id);
            } else {
                throw new ClientNotFoundException(id);
            }
        } else {
            throw new NoAccessException(id);
        }
        return id;
    }

    @Autowired
    public void setClientMapper(ClientMapper clientMapper) {
        this.clientMapper = clientMapper;
    }

    @Autowired
    public void setClientRepo(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
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
