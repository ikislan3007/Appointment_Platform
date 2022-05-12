package com.example.appointmentApp.domain.activity.repository;

import com.example.appointmentApp.domain.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findByProviderId(Long providerId);
}
