package com.example.mortgage.service;

import com.example.mortgage.dto.MortgageApplicationRequest;
import com.example.mortgage.exception.ResourceNotFoundException;
import com.example.mortgage.model.MortgageApplication;
import com.example.mortgage.model.User;
import com.example.mortgage.repository.MortgageApplicationRepository;
import com.example.mortgage.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class MortgageApplicationService {
    private final MortgageApplicationRepository mortgageApplicationRepository;
    private final UserRepository userRepository;
    public MortgageApplicationService(MortgageApplicationRepository mortgageApplicationRepository, UserRepository userRepository) {
        this.mortgageApplicationRepository = mortgageApplicationRepository;
        this.userRepository = userRepository;
    }

    public MortgageApplication createApplication(MortgageApplicationRequest mortgageApplicationRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        MortgageApplication application = new MortgageApplication();
        application.setApplicantName(mortgageApplicationRequest.getApplicantName());
        application.setAmount(mortgageApplicationRequest.getAmount());
        application.setApplicant(user);
        return mortgageApplicationRepository.save(application);
    }

    public MortgageApplication updateApplicationStatus(Long applicationId, MortgageApplication.ApplicationStatus status) {
        MortgageApplication application = mortgageApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Mortgage application not found"));
        application.setStatus(status);
        return mortgageApplicationRepository.save(application);
    }

    public void deleteApplication(Long applicationId) {
        MortgageApplication application = mortgageApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Mortgage application not found"));
        mortgageApplicationRepository.delete(application);
    }

}
