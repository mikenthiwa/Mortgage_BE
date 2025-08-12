package com.example.mortgage.dto;

import com.example.mortgage.model.MortgageApplication;

public class MortgageApplicationDTO {

    private Long id;
    private String applicantName;
    private Double amount;
    private MortgageApplication.ApplicationStatus status;

    public MortgageApplicationDTO(Long id, String applicantName, Double amount, MortgageApplication.ApplicationStatus status) {
        this.id = id;
        this.applicantName = applicantName;
        this.amount = amount;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public MortgageApplication.ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(MortgageApplication.ApplicationStatus status) {
        this.status = status;
    }
}
