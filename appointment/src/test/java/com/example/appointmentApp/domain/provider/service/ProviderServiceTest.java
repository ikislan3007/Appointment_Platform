package com.example.appointmentApp.domain.provider.service;

import com.example.appointmentApp.domain.employee.repository.EmployeeRepository;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.provider.mapper.ProviderMapper;
import com.example.appointmentApp.domain.provider.models.ProviderCreateDTO;
import com.example.appointmentApp.domain.provider.models.ProviderResponseDTO;
import com.example.appointmentApp.domain.provider.models.ProviderUpdateDTO;
import com.example.appointmentApp.domain.provider.repository.ProviderRepository;
import com.example.appointmentApp.infrastructure.custom.provider.ProviderNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalTime;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProviderServiceTest {
    @InjectMocks
    private ProviderServiceImpl providerService;
    @Mock
    private ProviderMapper providerMapper;
    @Mock
    private ProviderRepository providerRepo;
    @Mock
    private EmployeeRepository employeeRepo;
    Provider provider;
    ProviderResponseDTO providerResponseDTO;


    @Before
    public void setUp() {
        provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        providerResponseDTO = new ProviderResponseDTO(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
    }

    @Test
    public void getProviderByIdTest() {
        doReturn(Optional.of(provider)).when(providerRepo).findById(provider.getId());
        doReturn(providerResponseDTO).when(providerMapper).map(provider);
        ProviderResponseDTO providerResponseDTOFromService = this.providerService.get(1L);
        assertThat(providerResponseDTOFromService.id()).isEqualTo(provider.getId());
    }

    @Test
    public void getProviderByIdNotExistingIdShouldReturnProviderNotFoundException() {
        Mockito.when(providerRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ProviderNotFoundException.class, () -> providerService.get(1L));
    }

    @Test
    public void findAllProviders() {
        int pageNumber = 0;
        int pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Provider> providerPage = new PageImpl<>(Collections.singletonList(provider));
        when(providerRepo.findAll(pageable)).thenReturn(providerPage);
        Page<Provider> providers = providerRepo.findAll(pageable);
        assertEquals(1, providers.getNumberOfElements());
    }

    @Test
    public void deleteProviderTest() {
        Mockito.when(providerRepo.existsById(provider.getId())).thenReturn(true);
        doNothing().when(providerRepo).delete(provider);
        providerService.delete(provider.getId());
        assertThat(providerService.delete(provider.getId())).isEqualTo(provider.getId());
    }

    @Test
    public void deleteProviderShouldReturnProviderNotFoundException() {
        Mockito.when(providerRepo.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(employeeRepo.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ProviderNotFoundException.class, () -> providerService.delete(1L));
    }

    @Test
    public void createProviderTest() {
        ProviderCreateDTO providerCreateDTO = new ProviderCreateDTO("provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        doReturn(provider).when(providerMapper).map(providerCreateDTO);
        Provider createdProvider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        doReturn(createdProvider).when(providerRepo).save(provider);
        doReturn(providerResponseDTO).when(providerMapper).map(createdProvider);
        ProviderResponseDTO providerResponseDTOForTest = providerService.save(providerCreateDTO);
        assertThat(providerResponseDTOForTest.name()).isEqualTo(providerCreateDTO.name());
    }


    @Test
    public void updateProviderTest() {
        ProviderUpdateDTO providerUpdateDTO = new ProviderUpdateDTO("provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        Provider providerForUpdate = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        doReturn(Optional.of(provider)).when(providerRepo).findById(1L);
        doReturn(providerForUpdate).when(providerMapper).map(providerUpdateDTO);
        doReturn(providerResponseDTO).when(providerMapper).map(providerForUpdate);
        doReturn(providerForUpdate).when(providerRepo).save(providerForUpdate);
        ProviderResponseDTO providerResponseDTOForTest = providerService.update(providerUpdateDTO, 1L);
        assertThat(providerResponseDTOForTest.name()).isEqualTo(providerResponseDTO.name());
        assertThat(providerResponseDTOForTest.website()).isEqualTo(providerResponseDTO.website());
        assertThat(providerResponseDTOForTest.domain()).isEqualTo(providerResponseDTO.domain());
    }

}
