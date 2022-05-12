package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.client.models.ClientCreateDTO;
import com.example.appointmentApp.domain.client.models.ClientResponseDTO;
import com.example.appointmentApp.domain.client.models.ClientUpdateDTO;
import com.example.appointmentApp.domain.client.service.ClientService;
import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.infrastructure.security.JwtRequestFilter;
import com.example.appointmentApp.infrastructure.security.MyAccountDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ClientController.class)
public class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientService clientService;
    @MockBean
    MyAccountDetailsService accountDetailsService;
    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @Test
    @WithMockUser(roles = "CLIENT")
    void getClientByIdTest() throws Exception {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "admin");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(1L, "+1 1234567890123", "Ul.Nikolay Rakitin 23a", accountResponseDTO);

        when(clientService.get(anyLong())).thenReturn(clientResponseDTO);
        mockMvc.perform(get("/client/1", 1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("+1 1234567890123"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value("Ul.Nikolay Rakitin 23a"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void saveClientTest() throws Exception {
        Role role = new Role("account");
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO("Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", role);
        ClientCreateDTO clientCreateDTO = new ClientCreateDTO("+1 1234567890123", "Ul.Nikolay Rakitin 23a", accountCreateDTO);
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "user");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Grigorova Inna", "inna_grigorov@gmail.com", "Qythju143!*", roleResponseDTO);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(1L, "+1 1234567890123", "Ul.Nikolay Rakitin 23a", accountResponseDTO);

        when(clientService.save(clientCreateDTO)).thenReturn(clientResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/client")
                .content(new ObjectMapper().writeValueAsString(clientResponseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void updateClientTest() throws Exception {
        ClientUpdateDTO clientUpdateDTO = new ClientUpdateDTO("+1 1234567890123", "Ul.Nikolay Rakitin 23a");
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "user");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Grigorova Inna", "inna_grigorov@gmail.com", "Qythju143!*", roleResponseDTO);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(1L, "+1 1234567890123", "Ul.Nikolay Rakitin 23a", accountResponseDTO);

        when(clientService.update(clientUpdateDTO, 1L)).thenReturn(clientResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.patch("/client/1", 1L)
                        .content(new ObjectMapper().writeValueAsString(clientResponseDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllClientsTest() throws Exception {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "user");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Grigorova Inna", "inna_grigorov@gmail.com", "Qythju143!*", roleResponseDTO);
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO(1L, "+1 1234567890123", "Ul.Nikolay Rakitin 23a", accountResponseDTO);

        when(clientService.get(anyLong())).thenReturn(clientResponseDTO);
        mockMvc.perform(get("/client?page=1&size=3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
