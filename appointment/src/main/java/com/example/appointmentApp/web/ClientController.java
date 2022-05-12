package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.client.models.ClientCreateDTO;
import com.example.appointmentApp.domain.client.models.ClientResponseDTO;
import com.example.appointmentApp.domain.client.models.ClientUpdateDTO;
import com.example.appointmentApp.domain.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/client")
public class ClientController {
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@Valid @RequestBody ClientCreateDTO clientCreateDTO) {
        return ResponseEntity.ok(clientService.save(clientCreateDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.get(id));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ClientResponseDTO>> get(Pageable pageable) {
        return ResponseEntity.ok(clientService.getAll(pageable));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PatchMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ClientUpdateDTO clientUpdateDTO) {
        ClientResponseDTO clientResponseDTO = clientService.update(clientUpdateDTO, id);
        return ResponseEntity.ok(clientResponseDTO);
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

}

