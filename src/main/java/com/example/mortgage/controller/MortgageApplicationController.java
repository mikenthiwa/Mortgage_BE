package com.example.mortgage.controller;

import com.example.mortgage.dto.MortgageApplicationRequest;
import com.example.mortgage.model.MortgageApplication;
import com.example.mortgage.service.MortgageApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mortgage")
public class MortgageApplicationController {
    private final MortgageApplicationService mortgageApplicationService;

    public  MortgageApplicationController(MortgageApplicationService mortgageApplicationService) {
        this.mortgageApplicationService = mortgageApplicationService;
    }

    @PostMapping("/user/{userId}/application")
    public ResponseEntity<MortgageApplication> createApplication(@PathVariable Long userId, @RequestBody MortgageApplicationRequest request) {
        return ResponseEntity.ok(mortgageApplicationService.createApplication(request, userId));
    }

    @PutMapping("/application/{applicationId}/status")
    public ResponseEntity<MortgageApplication> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam MortgageApplication.ApplicationStatus status) {
        return ResponseEntity.ok(mortgageApplicationService.updateApplicationStatus(applicationId, status));
    }

    @DeleteMapping("/application/{applicationId}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long applicationId) {
        mortgageApplicationService.deleteApplication(applicationId);
        return ResponseEntity.noContent().build();
    }
}
