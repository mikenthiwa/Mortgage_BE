package controller;

import com.example.mortgage.event.KafkaPublisher;
import com.example.mortgage.model.MortgageApplication;
import com.example.mortgage.model.User;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyLong;

import org.mockito.*;

import java.util.Optional;

import com.example.mortgage.mortgage.MortgageApplicationRequest;
import com.example.mortgage.mortgage.MortgageRepository;
import com.example.mortgage.mortgage.MortgageService;
import com.example.mortgage.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class MortgageServiceTest {
    @Mock private MortgageRepository mortgageRepository;
    @Mock private UserRepository userRepository;
    @Mock private KafkaPublisher kafkaPublisher;

    @InjectMocks private MortgageService mortgageService;

    @Test
    void createApplication_succeeds_andPublishes() {
        Long userId = 2L;
        var request = new MortgageApplicationRequest();
        request.setApplicantName("Test User");
        request.setAmount(250000.0);

        var user = new User();
        user.setId(userId);


        var saved = new MortgageApplication();
        saved.setId(123L);
        saved.setApplicantName(request.getApplicantName());
        saved.setAmount(request.getAmount());
        saved.setApplicant(user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mortgageRepository.save(any(MortgageApplication.class))).thenReturn(saved);

        var result = mortgageService.createApplication(request, userId);
        assertThat(result.getId()).isEqualTo(saved.getId());
        assertThat(result.getApplicantName()).isEqualTo(saved.getApplicantName());
        assertThat(result.getAmount()).isEqualTo(saved.getAmount());
        verify(kafkaPublisher, times(1)).publish(
                eq("mortgage-application-created"),
                contains("Created application ID: " + saved.getId())
        );
        verify(mortgageRepository).save(any(MortgageApplication.class));
    }

    @Test
    void createApplication_userNotFound_throwsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        var request = new MortgageApplicationRequest();
        request.setApplicantName("Test User");
        request.setAmount(250000.0);
        try {
            mortgageService.createApplication(request, 1L);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(com.example.mortgage.exception.ResourceNotFoundException.class)
                    .hasMessageContaining("User not found");
        }
        verify(mortgageRepository, never()).save(any(MortgageApplication.class));
        verify(kafkaPublisher, never()).publish(anyString(), anyString());
    }

    @Test
    void createApplication_unexpectedError_throwsUnhandledException() {
        Long userId = 2L;
        var request = new MortgageApplicationRequest();
        request.setApplicantName("Test User");
        request.setAmount(250000.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(mortgageRepository.save(any(MortgageApplication.class))).thenThrow(new RuntimeException("Unexpected error"));

        try {
            mortgageService.createApplication(request, userId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(com.example.mortgage.exception.UnhandledException.class)
                    .hasMessageContaining("Something went wrong while creating the mortgage application");
        }
        verify(kafkaPublisher, never()).publish(anyString(), anyString());
        verify(mortgageRepository).save(any(MortgageApplication.class));
    }

    @Test
    void updateApplication_succeeds_andPublishes() {
        Long applicationId = 123L;
        var request = new MortgageApplicationRequest();
        request.setApplicantName("Updated User");
        request.setAmount(300000.0);

        var existingApplication = new MortgageApplication();
        existingApplication.setId(applicationId);
        existingApplication.setApplicantName("Old User");
        existingApplication.setAmount(250000.0);

        when(mortgageRepository.findById(applicationId)).thenReturn(Optional.of(existingApplication));
        when(mortgageRepository.save(any(MortgageApplication.class))).thenReturn(existingApplication);

        var result = mortgageService.updateApplicationStatus(applicationId, MortgageApplication.ApplicationStatus.APPROVED);
        assertThat(result.getId()).isEqualTo(existingApplication.getId());
        assertThat(result.getApplicantName()).isEqualTo(existingApplication.getApplicantName());
        assertThat(result.getAmount()).isEqualTo(existingApplication.getAmount());
        assertThat(result.getStatus()).isEqualTo(MortgageApplication.ApplicationStatus.APPROVED);
    }

    @Test
    void updateApplication_applicationNotFound_throwsException() {
        Long applicationId = 123L;
        when(mortgageRepository.findById(applicationId)).thenReturn(Optional.empty());
        try {
            mortgageService.updateApplicationStatus(applicationId, MortgageApplication.ApplicationStatus.APPROVED);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(com.example.mortgage.exception.ResourceNotFoundException.class)
                    .hasMessageContaining("Mortgage application not found");
        }
        verify(mortgageRepository, never()).save(any(MortgageApplication.class));
        verify(kafkaPublisher, never()).publish(anyString(), anyString());
    }

    @Test
    void updateApplication_unexpectedError_throwsUnhandledException() {
        Long applicationId = 123L;
        when(mortgageRepository.findById(applicationId)).thenThrow(new RuntimeException("Unexpected error"));
        try {
            mortgageService.updateApplicationStatus(applicationId, MortgageApplication.ApplicationStatus.APPROVED);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(com.example.mortgage.exception.UnhandledException.class)
                    .hasMessageContaining("Something went wrong while updating the mortgage application");
        }
        verify(mortgageRepository, never()).save(any(MortgageApplication.class));
        verify(kafkaPublisher, never()).publish(anyString(), anyString());
    }

    @Test
    void getAllApplications_succeeds() {
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        var application1 = new MortgageApplication();
        application1.setId(1L);
        application1.setApplicantName("User One");
        application1.setAmount(200000.0);
        application1.setStatus(MortgageApplication.ApplicationStatus.PENDING);
        var application2 = new MortgageApplication();
        application2.setId(2L);
        application2.setApplicantName("User Two");
        application2.setAmount(300000.0);
        application2.setStatus(MortgageApplication.ApplicationStatus.APPROVED);
        var applications = new org.springframework.data.domain.PageImpl<>(
                java.util.List.of(application1, application2),
                pageable,
                2
        );
        when(mortgageRepository.findAll(pageable)).thenReturn(applications);
        var result = mortgageService.getAllApplications(pageable);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(application1.getId());
        assertThat(result.getContent().get(0).getApplicantName()).isEqualTo(application1.getApplicantName());
        assertThat(result.getContent().get(0).getAmount()).isEqualTo(application1.getAmount());
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(application1.getStatus());
        assertThat(result.getContent().get(1).getId()).isEqualTo(application2.getId());
        assertThat(result.getContent().get(1).getApplicantName()).isEqualTo(application2.getApplicantName());
    }

    @Test
    void getAllApplications_unexpectedError_throwsUnhandledException() {
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        when(mortgageRepository.findAll(pageable)).thenThrow(new RuntimeException("Unexpected error"));
        try {
            mortgageService.getAllApplications(pageable);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(com.example.mortgage.exception.UnhandledException.class)
                    .hasMessageContaining("Something went wrong while fetching mortgage applications");
        }
    }

    @Test
    void deleteApplication_succeeds() {
        Long applicationId = 123L;
        var existingApplication = new MortgageApplication();
        existingApplication.setId(applicationId);
        when(mortgageRepository.findById(applicationId)).thenReturn(Optional.of(existingApplication));
        mortgageService.deleteApplication(applicationId);
        verify(mortgageRepository, times(1)).delete(existingApplication);
    }

    @Test
    void deleteApplication_applicationNotFound_throwsException() {
        Long applicationId = 123L;
        when(mortgageRepository.findById(applicationId)).thenReturn(Optional.empty());
        try {
            mortgageService.deleteApplication(applicationId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(com.example.mortgage.exception.ResourceNotFoundException.class)
                    .hasMessageContaining("Mortgage application not found");
        }
    }


}
