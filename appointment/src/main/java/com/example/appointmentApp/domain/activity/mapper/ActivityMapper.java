package com.example.appointmentApp.domain.activity.mapper;

import com.example.appointmentApp.domain.activity.entity.Activity;
import com.example.appointmentApp.domain.activity.models.ActivityCreateDTO;
import com.example.appointmentApp.domain.activity.models.ActivityResponseDTO;
import com.example.appointmentApp.domain.activity.models.ActivityUpdateDTO;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.provider.models.ProviderResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ActivityMapper {
    Activity map(ActivityCreateDTO activityCreateDTO);
    Activity map(ActivityUpdateDTO activityUpdateDTO);
    ActivityResponseDTO map(Activity activity);
    ProviderResponseDTO map(Provider provider);
    void update(@MappingTarget Activity activity, ActivityUpdateDTO activityUpdateDTO);
}

