package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.role.models.RoleCreateDTO;
import com.example.appointmentApp.domain.role.models.RoleResponseDTO;
import com.example.appointmentApp.domain.role.service.RoleService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = RoleController.class)
class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoleService roleService;
    @MockBean
    MyAccountDetailsService myAccountDetailsService;
    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @WithMockUser(roles = "ADMIN")
    @Test
    void createRoleTest() throws Exception {
        RoleCreateDTO roleCreateDTO = new RoleCreateDTO("user");
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "user");
        when(roleService.save(roleCreateDTO)).thenReturn(roleResponseDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/role")
                .content(new ObjectMapper().writeValueAsString(roleResponseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleName").value("user"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getRoleByIdTest() throws Exception {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "admin");

        when(roleService.get(anyLong())).thenReturn(roleResponseDTO);
        mockMvc.perform(get("/role/1", 1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.roleName").value("admin"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllRolesTest() throws Exception {
        RoleResponseDTO roleResponseDTO = new RoleResponseDTO(1L, "Admin");
        when(roleService.get(anyLong())).thenReturn(roleResponseDTO);
        mockMvc.perform(get("/role?page=1&size=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}
