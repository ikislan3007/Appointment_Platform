package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeCreateDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeResponseDTO;
import com.example.appointmentApp.domain.employee.model.EmployeeUpdateDTO;
import com.example.appointmentApp.domain.employee.service.EmployeeService;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    MyAccountDetailsService accountDetailsService;
    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void getEmployeeByIdTest() throws Exception {
        Provider provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503", LocalTime.of(8, 7, 7), LocalTime.of(23, 59, 59), "Monday");
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L, "employee", "+1 1234567810123", 5.00, provider, accountResponseDTO);
        when(employeeService.get(anyLong())).thenReturn(employeeResponseDTO);
        mockMvc.perform(get("/employee/1", 1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("employee"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("+1 1234567810123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ratePerHour").value(5.0))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void saveEmployeeTest() throws Exception {
        Role role = new Role(1L, "account");
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO("Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", role);
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        Provider provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503", LocalTime.of(8, 7, 7), LocalTime.of(23, 59, 59), "Monday");
        EmployeeCreateDTO employeeCreateDTO = new EmployeeCreateDTO("employee", "+1 1234567810123", 5.00, "provider", accountCreateDTO);
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L, "employee", "+1 1234567810123", 5.00, provider, accountResponseDTO);
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        when(employeeService.save(employeeCreateDTO)).thenReturn(employeeResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/employee")
                        .content(mapper.writeValueAsString(employeeResponseDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());
    }

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void updateEmployeeTest() throws Exception {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        Provider provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503", LocalTime.of(8, 7, 7), LocalTime.of(23, 59, 59), "Monday");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L, "employee", "+1 1234567810123", 5.00, provider, accountResponseDTO);
        EmployeeUpdateDTO employeeUpdateDTO = new EmployeeUpdateDTO("employee", "+1 1234567810123", 5.00);
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        when(employeeService.update(employeeUpdateDTO, 1L)).thenReturn(employeeResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch("/employee/1", 1L)
                        .content(mapper.writeValueAsString(employeeResponseDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("employee"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("+1 1234567810123"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.ratePerHour").value(5.0))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void getAllEmployeesTest() throws Exception {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        Provider provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503", LocalTime.of(8, 7, 7), LocalTime.of(23, 59, 59), "Monday");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        EmployeeResponseDTO employeeResponseDTO = new EmployeeResponseDTO(1L, "employee", "+1 1234567810123", 5.00, provider, accountResponseDTO);
        when(employeeService.get(anyLong())).thenReturn(employeeResponseDTO);
        mockMvc.perform(get("/employee?page=1&size=3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
