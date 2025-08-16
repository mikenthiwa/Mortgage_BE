package com.example.mortgage.mortgage;

import com.example.mortgage.extension.PaginationResponse;
import com.example.mortgage.exception.ResourceNotFoundException;
import com.example.mortgage.exception.UnauthorizedException;
import com.example.mortgage.model.MortgageApplication;
import com.example.mortgage.model.User;
import com.example.mortgage.user.UserRepository;
import com.example.mortgage.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@Tag(name = "Mortgage Application", description = "Endpoints for managing mortgage applications")
@RestController
@RequestMapping("/api/mortgage")
class MortgageController {
    private final MortgageService mortgageApplicationService;
    private final UserRepository userRepository;

    public MortgageController(MortgageService mortgageApplicationService, UserRepository userRepository) {
        this.mortgageApplicationService = mortgageApplicationService;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Create a new mortgage application")
    @CrossOrigin
    @PostMapping("/user/{userId}/application")
    public ResponseEntity<ApiResponse> createApplication(
            @PathVariable Long userId,
            @RequestBody MortgageApplicationRequest request) {

        mortgageApplicationService.createApplication(request, userId);
        ApiResponse response = ApiResponse.success(
                "Application submitted successfully", 201, null
        );
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "Get all mortgage applications")
    @CrossOrigin
    @GetMapping("/applications")
    public ResponseEntity<PaginationResponse<MortgageDTO>> getAllApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<MortgageDTO> result = mortgageApplicationService.getAllApplications(pageable);
        var responseBody  = new PaginationResponse<>(result, "Applications retrieved successfully");
        return ResponseEntity.status(200).body(responseBody);
    }

    @Operation(summary = "Update the status of a mortgage application")
    @CrossOrigin
    @PutMapping("/application/{applicationId}/status")
    public ResponseEntity<ApiResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam Long userId,
            @RequestParam MortgageApplication.ApplicationStatus status) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ADMIN"));

        if (!isAdmin) {
            throw new UnauthorizedException("Unauthorized access");
        }

        MortgageDTO updated = mortgageApplicationService.updateApplicationStatus(applicationId, status);

        String message = "Application status updated to " + updated.getStatus().name();
        ApiResponse response = ApiResponse.success(message, 200, updated);

        return ResponseEntity.status(response.statusCode).body(response);
    }

    @Operation(summary = "Delete a mortgage application")
    @CrossOrigin
    @DeleteMapping("/application/{applicationId}")
    public ResponseEntity<ApiResponse> deleteApplication(@PathVariable Long applicationId, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().name().equals("ADMIN"));

        if (!isAdmin) {
            throw new UnauthorizedException("Unauthorized access");
        }

        mortgageApplicationService.deleteApplication(applicationId);
        ApiResponse response = ApiResponse.success("Application deleted successfully", 204, null);
        return ResponseEntity.status(response.statusCode).body(response);
    }
}
