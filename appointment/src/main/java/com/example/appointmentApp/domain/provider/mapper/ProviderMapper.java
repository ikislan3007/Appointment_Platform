package com.example.appointmentApp.domain.provider.mapper;

import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.provider.models.ProviderCreateDTO;
import com.example.appointmentApp.domain.provider.models.ProviderResponseDTO;
import com.example.appointmentApp.domain.provider.models.ProviderUpdateDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProviderMapper {
    Provider map(ProviderCreateDTO providerCreateDTO);
    Provider map(ProviderUpdateDTO providerUpdateDTO);
    ProviderResponseDTO map(Provider provider);
}
