package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.provider.models.ProviderCreateDTO;
import com.example.appointmentApp.domain.provider.models.ProviderResponseDTO;
import com.example.appointmentApp.domain.provider.models.ProviderUpdateDTO;
import com.example.appointmentApp.domain.provider.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/provider")
public class ProviderController {
    private ProviderService providerService;

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @PostMapping
    public ResponseEntity<ProviderResponseDTO> create(@Valid @RequestBody ProviderCreateDTO provider) {
        return ResponseEntity.ok(providerService.save(provider));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    public ResponseEntity<ProviderResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.get(id));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(providerService.delete(id));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<ProviderResponseDTO>> get(Pageable pageable) {
        return ResponseEntity.ok(providerService.getAll(pageable));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProviderResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ProviderUpdateDTO providerUpdateDTO) {
        ProviderResponseDTO providerResponseDTO = providerService.update(providerUpdateDTO, id);
        return ResponseEntity.ok(providerResponseDTO);
    }

    @Autowired
    public void setProviderService(ProviderService providerService) {
        this.providerService = providerService;
    }
}
