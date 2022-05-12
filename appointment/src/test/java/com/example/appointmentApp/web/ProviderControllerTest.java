package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.provider.models.ProviderCreateDTO;
import com.example.appointmentApp.domain.provider.models.ProviderResponseDTO;
import com.example.appointmentApp.domain.provider.models.ProviderUpdateDTO;
import com.example.appointmentApp.domain.provider.service.ProviderService;
import com.example.appointmentApp.infrastructure.security.JwtRequestFilter;
import com.example.appointmentApp.infrastructure.security.MyAccountDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalTime;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ProviderController.class)
public class ProviderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProviderService providerService;
    @MockBean
    MyAccountDetailsService accountDetailsService;
    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void getProviderByIdTest() throws Exception {
        ProviderResponseDTO providerResponseDTO = new ProviderResponseDTO(1L, "provider", "http://www.provider.com", "provider", "+359 877544503", LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        when(providerService.get(anyLong())).thenReturn(providerResponseDTO);
        mockMvc.perform(get("/provider/1", 1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("provider"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.website").value("http://www.provider.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.domain").value("provider"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("+359 877544503"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startOfTheWorkingDay").value("08:07:07"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endOfTheWorkingDay").value("23:59:59"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workingDays").value("Monday"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void saveProviderTest() throws Exception {
        ProviderCreateDTO providerCreateDTO = new ProviderCreateDTO("provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        ProviderResponseDTO providerResponseDTO = new ProviderResponseDTO(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        when(providerService.save(providerCreateDTO)).thenReturn(providerResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/provider")
                .content(mapper.writeValueAsString(providerResponseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("provider"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.website").value("http://www.provider.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.domain").value("provider"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("+359 877544503"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startOfTheWorkingDay").value("08:07:07"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.endOfTheWorkingDay").value("23:59:59"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.workingDays").value("Monday"));

    }

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void updateProviderTest() throws Exception {
        ProviderUpdateDTO providerUpdateDTO = new ProviderUpdateDTO("provider", "http://www.provider", "provider", "+359 877544503",  LocalTime.of(8,7,7), LocalTime.of(23,59,59),"Monday");
        ProviderResponseDTO providerResponseDTO = new ProviderResponseDTO(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",   LocalTime.of(8,7,7), LocalTime.of(23,59,59) , "Monday");
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        when(providerService.update(providerUpdateDTO, 1L)).thenReturn(providerResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/provider/1", 1L)
                .content(mapper.writeValueAsString(providerResponseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void getAllProvidersTest() throws Exception {
        ProviderResponseDTO providerResponseDTO = new ProviderResponseDTO(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        when(providerService.get(anyLong())).thenReturn(providerResponseDTO);
        mockMvc.perform(get("/provider?page=1&size=3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
