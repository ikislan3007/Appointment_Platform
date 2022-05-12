package com.example.appointmentApp.web;

import com.example.appointmentApp.domain.activity.models.ActivityCreateDTO;
import com.example.appointmentApp.domain.activity.models.ActivityResponseDTO;
import com.example.appointmentApp.domain.activity.models.ActivityUpdateDTO;
import com.example.appointmentApp.domain.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private ActivityService activityService;

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @PostMapping
    public ResponseEntity<ActivityResponseDTO> create(@Valid @RequestBody ActivityCreateDTO activityCreateDTO) {
        return ResponseEntity.ok(activityService.save(activityCreateDTO));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PROVIDER_ADMIN')"+"||hasRole('CLIENT')")
    public ResponseEntity<ActivityResponseDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.get(id));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> delete(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.delete(id));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping
    public ResponseEntity<Page<ActivityResponseDTO>> get(Pageable pageable) {
        return ResponseEntity.ok(activityService.getAll(pageable));
    }

    @PreAuthorize("hasRole('PROVIDER_ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<ActivityResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ActivityUpdateDTO activityUpdateDTO) {
        ActivityResponseDTO activityResponseDTO = activityService.update(activityUpdateDTO, id);
        return ResponseEntity.ok(activityResponseDTO);
    }
    @Autowired
    public void setActivityService(ActivityService activityService) {
        this.activityService = activityService;
    }
}

