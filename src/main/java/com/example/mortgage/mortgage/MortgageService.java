package com.example.mortgage.mortgage;

import com.example.mortgage.event.KafkaPublisher;
import com.example.mortgage.exception.ResourceNotFoundException;
import com.example.mortgage.exception.UnhandledException;
import com.example.mortgage.model.MortgageApplication;
import com.example.mortgage.model.User;
import com.example.mortgage.user.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MortgageService {
    private final MortgageRepository mortgageApplicationRepository;
    private final UserRepository userRepository;
    private final KafkaPublisher kafkaPublisher;

    public MortgageService(MortgageRepository mortgageApplicationRepository, UserRepository userRepository, KafkaPublisher kafkaPublisher
    ) {
        this.mortgageApplicationRepository = mortgageApplicationRepository;
        this.userRepository = userRepository;
        this.kafkaPublisher = kafkaPublisher;
    }

    public MortgageApplication createApplication(MortgageApplicationRequest mortgageApplicationRequest, Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            MortgageApplication application = new MortgageApplication();
            application.setApplicantName(mortgageApplicationRequest.getApplicantName());
            application.setAmount(mortgageApplicationRequest.getAmount());
            application.setApplicant(user);
            var createdApplication = mortgageApplicationRepository.save(application);
            kafkaPublisher.publish("mortgage-application-created", "Created application ID: " + createdApplication.getId());
            return createdApplication;

        }
        catch (ResourceNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new UnhandledException("Something went wrong while creating the mortgage application");
        }

    }

    public Page<MortgageDTO> getAllApplications(Pageable pageable) {
        try {
            return mortgageApplicationRepository
                .findAll(pageable)
                .map(app -> new MortgageDTO(
                        app.getId(),
                        app.getApplicantName(),
                        app.getAmount(),
                        app.getStatus()
                ));
        } catch (Exception e) {
            throw new UnhandledException("Something went wrong while fetching mortgage applications");
        }

    }

    public MortgageDTO updateApplicationStatus(Long applicationId, MortgageApplication.ApplicationStatus status) {
        try {
            MortgageApplication application = mortgageApplicationRepository.findById(applicationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Mortgage application not found"));
            application.setStatus(status);
            MortgageApplication saved = mortgageApplicationRepository.save(application);
            MortgageDTO updatedAppStatus = new MortgageDTO(
                    saved.getId(),
                    saved.getApplicantName(),
                    saved.getAmount(),
                    saved.getStatus()
            );
            kafkaPublisher.publish("mortgage-application-updated", "Updated application ID: " + saved.getId() + " to status: " + status);
            return updatedAppStatus;
        }
        catch (ResourceNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new UnhandledException("Something went wrong while updating the mortgage application status");
        }

    }

    public void deleteApplication(Long applicationId) {
        try {
            MortgageApplication application = mortgageApplicationRepository.findById(applicationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Mortgage application not found"));
            mortgageApplicationRepository.delete(application);
            kafkaPublisher.publish("mortgage-application-deleted", "Deleted application ID: " + application.getId());
        }
        catch (ResourceNotFoundException e) {
            throw e;
        }
        catch (Exception e) {
            throw new UnhandledException("Something went wrong while deleting the mortgage application");
        }

    }

}
