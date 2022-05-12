package com.example.appointmentApp.web;


import com.example.appointmentApp.domain.account.entity.Account;
import com.example.appointmentApp.domain.activity.models.ActivityCreateDTO;
import com.example.appointmentApp.domain.activity.models.ActivityResponseDTO;
import com.example.appointmentApp.domain.activity.service.ActivityService;
import com.example.appointmentApp.domain.employee.entity.Employee;
import com.example.appointmentApp.domain.provider.entity.Provider;
import com.example.appointmentApp.domain.role.entity.Role;
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
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ActivityController.class)
public class ActivityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ActivityService activityService;
    @MockBean
    MyAccountDetailsService accountDetailsService;
    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void getActivityByIdTest() throws Exception {
        Duration duration = Duration.parse("P3DT5H40M30S");
        Provider provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503", LocalTime.of(8, 7, 7), LocalTime.of(23, 59, 59), "Monday");
        Role role = new Role(1L, "PROVIDER_ADMIN");
        Account account = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",role);
        Employee employee=new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(employee);
        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO(1L, "activity", 220, duration, provider,employeeList);

        when(activityService.get(anyLong())).thenReturn(activityResponseDTO);
        mockMvc.perform(get("/activity/1", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void saveActivityTest() throws Exception {
        Duration duration = Duration.parse("P3DT5H40M30S");
        List<Long> employeesId=new ArrayList<>();
        employeesId.add(1L);
        employeesId.add(2L);
        ActivityCreateDTO activityCreateDTO = new ActivityCreateDTO("activity",220,duration, "provider",employeesId);
        Role role = new Role(1L, "PROVIDER_ADMIN");
        Account account = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",role);
        Provider provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        Employee employee=new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(employee);
        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO(1L, "activity", 220, duration, provider,employeeList);

        when(activityService.save(activityCreateDTO)).thenReturn(activityResponseDTO);
        ObjectMapper mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/activity")
                        .content(mapper.writeValueAsString(activityResponseDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @WithMockUser(roles = "CLIENT")
    void getAllProvidersTest() throws Exception {
        Duration duration = Duration.parse("P3DT5H40M30S");
        Provider provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503", LocalTime.of(8, 7, 7), LocalTime.of(23, 59, 59), "Monday");
        Role role = new Role(1L, "PROVIDER_ADMIN");
        Account account = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",role);
        provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503",  LocalTime.of(8,7,7),  LocalTime.of(23,59,59), "Monday");
        Employee employee=new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(employee);
        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO(1L, "activity", 220, duration, provider,employeeList);

        when(activityService.get(anyLong())).thenReturn(activityResponseDTO);
        mockMvc.perform(get("/activity?page=1&size=3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "PROVIDER_ADMIN")
    void getProviderByIdTest() throws Exception {
        Duration duration = Duration.parse("P3DT5H40M30S");
        Provider provider = new Provider(1L, "provider", "http://www.provider.com", "provider", "+359 877544503", LocalTime.of(8, 7, 7), LocalTime.of(23, 59, 59), "Monday");
        Role role = new Role(1L, "PROVIDER_ADMIN");
        Account account = new Account(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan",role);
        Employee employee=new Employee(1L,"employee", "+1 1234567810123", 5.00,provider,  account);
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(employee);
        ActivityResponseDTO activityResponseDTO = new ActivityResponseDTO(1L, "activity", 220, duration, provider,employeeList);

        when(activityService.get(anyLong())).thenReturn(activityResponseDTO);
        mockMvc.perform(get("/activity/1", 1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("activity"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}
