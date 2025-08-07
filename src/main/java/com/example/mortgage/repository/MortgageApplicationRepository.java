package com.example.mortgage.repository;

import com.example.mortgage.model.MortgageApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MortgageApplicationRepository extends JpaRepository<MortgageApplication, Long> {
    List<MortgageApplication> findByApplicantId(Long applicantId);
    List<MortgageApplication> findByStatus(MortgageApplication.ApplicationStatus status);
}
