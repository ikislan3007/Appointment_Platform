package com.example.appointmentApp.domain.provider.repository;

import com.example.appointmentApp.domain.provider.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProviderRepository extends JpaRepository<Provider,Long> {
    Provider findByName(String providerName);
}
