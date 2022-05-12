package com.example.appointmentApp.domain.activity.service;

import com.example.appointmentApp.domain.activity.models.ActivityCreateDTO;
import com.example.appointmentApp.domain.activity.models.ActivityResponseDTO;
import com.example.appointmentApp.domain.activity.models.ActivityUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {
    ActivityResponseDTO save(ActivityCreateDTO activityCreateDTO);
    ActivityResponseDTO get(Long id);
    Page<ActivityResponseDTO> getAll(Pageable pageable);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ActivityResponseDTO update(ActivityUpdateDTO activityUpdateDTO, Long id);
    Long delete(Long id);
}