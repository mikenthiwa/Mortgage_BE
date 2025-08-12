package com.example.mortgage.service;

import com.example.mortgage.dto.MortgageApplicationRequest;
import com.example.mortgage.dto.MortgageApplicationDTO;
import com.example.mortgage.event.KafkaPublisher;
import com.example.mortgage.exception.ResourceNotFoundException;
import com.example.mortgage.model.MortgageApplication;
import com.example.mortgage.model.User;
import com.example.mortgage.repository.MortgageApplicationRepository;
import com.example.mortgage.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MortgageApplicationService {
    private final MortgageApplicationRepository mortgageApplicationRepository;
    private final UserRepository userRepository;
    private final KafkaPublisher kafkaPublisher;

    public MortgageApplicationService(MortgageApplicationRepository mortgageApplicationRepository, UserRepository userRepository, KafkaPublisher kafkaPublisher
    ) {
        this.mortgageApplicationRepository = mortgageApplicationRepository;
        this.userRepository = userRepository;
        this.kafkaPublisher = kafkaPublisher;
    }

    public MortgageApplication createApplication(MortgageApplicationRequest mortgageApplicationRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        MortgageApplication application = new MortgageApplication();
        application.setApplicantName(mortgageApplicationRequest.getApplicantName());
        application.setAmount(mortgageApplicationRequest.getAmount());
        application.setApplicant(user);
        kafkaPublisher.publish("mortgage-application-created", "Created application ID: " + application.getId());
        return mortgageApplicationRepository.save(application);
    }

    public Page<MortgageApplicationDTO> getAllApplications(Pageable pageable) {
        return mortgageApplicationRepository
                .findAll(pageable)
                .map(app -> new MortgageApplicationDTO(
                        app.getId(),
                        app.getApplicantName(),
                        app.getAmount(),
                        app.getStatus()
                ));
    }

    public MortgageApplicationDTO updateApplicationStatus(Long applicationId, MortgageApplication.ApplicationStatus status) {
        MortgageApplication application = mortgageApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Mortgage application not found"));
        application.setStatus(status);
        MortgageApplication saved = mortgageApplicationRepository.save(application);
        kafkaPublisher.publish("mortgage-application-updated", "Updated application ID: " + saved.getId() + " to status: " + status);
        return new MortgageApplicationDTO(
                saved.getId(),
                saved.getApplicantName(),
                saved.getAmount(),
                saved.getStatus()
        );
    }

    public void deleteApplication(Long applicationId) {
        MortgageApplication application = mortgageApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Mortgage application not found"));
        kafkaPublisher.publish("mortgage-application-deleted", "Deleted application ID: " + application.getId());
        mortgageApplicationRepository.delete(application);
    }

}
