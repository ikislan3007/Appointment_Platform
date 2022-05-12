package com.example.appointmentApp.domain.client.repository;

import com.example.appointmentApp.domain.client.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Client findByAccountEmail(String name);

}
