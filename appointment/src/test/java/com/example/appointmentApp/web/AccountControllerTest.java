package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.role.entity.Role;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.domain.account.models.AccountCreateDTO;
import com.example.appointmentApp.domain.account.models.AccountResponseDTO;
import com.example.appointmentApp.domain.account.models.AccountRoleAssignDTO;
import com.example.appointmentApp.domain.account.models.AccountUpdateDTO;
import com.example.appointmentApp.domain.account.service.AccountService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @MockBean
    MyAccountDetailsService accountDetailsService;
    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAccountByIdTest() throws Exception {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "admin");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        when(accountService.get(anyLong())).thenReturn(accountResponseDTO);
        mockMvc.perform(get("/account/1", 1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Ivanov"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ivanov.ivan@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("thisisapasswordivan"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void saveAccountTest() throws Exception {
        Role role = new Role("account");
        AccountCreateDTO accountCreateDTO = new AccountCreateDTO("Grigorova Inna", "inna_grigorov@gmail.com", "Qythju143!*", role);
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "user");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Grigorova Inna", "inna_grigorov@gmail.com", "Qythju143!*", roleResponseDTO);
        when(accountService.save(accountCreateDTO)).thenReturn(accountResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.post("/account")
                .content(new ObjectMapper().writeValueAsString(accountResponseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Grigorova Inna"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("inna_grigorov@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("Qythju143!*"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleName").value("user"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateAccountTest() throws Exception {
        AccountUpdateDTO accountUpdateDTO = new AccountUpdateDTO("Ivanov", "ivanov.ivan@gmail.com", "ThisIsATestPassword1!");
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "ThisIsATestPassword1!", roleResponseDTO);
        when(accountService.update(accountUpdateDTO, 1L)).thenReturn(accountResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/account/1", 1L)
                .content(new ObjectMapper().writeValueAsString(accountResponseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value("Ivanov"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ivanov.ivan@gmail.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("ThisIsATestPassword1!"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllAccountsTest() throws Exception {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        when(accountService.get(anyLong())).thenReturn(accountResponseDTO);
        mockMvc.perform(get("/account?page=1&size=3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addRoleToAccountTest() throws Exception {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO(1L, "Ivanov", "ivanov.ivan@gmail.com", "thisisapasswordivan", roleResponseDTO);
        AccountRoleAssignDTO accountRoleAssignDTO = new AccountRoleAssignDTO(1L, 1L);
        when(accountService.updateAccountRole(anyLong(), anyLong())).thenReturn(accountResponseDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/account/role", 2l)
                .content(new ObjectMapper().writeValueAsString(accountRoleAssignDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L));
    }
}
