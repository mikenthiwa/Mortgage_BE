package com.example.mortgage.dto;

import com.example.mortgage.model.MortgageApplication;

public class MortgageApplicationResponse {

    private Long id;
    private String applicantName;
    private Double amount;
    private MortgageApplication.ApplicationStatus status;
    private String applicantUsername;

    public MortgageApplicationResponse() {
    }

    public MortgageApplicationResponse(Long id, String applicantName, Double amount, MortgageApplication.ApplicationStatus status, String applicantUsername) {
        this.id = id;
        this.applicantName = applicantName;
        this.amount = amount;
        this.status = status;
        this.applicantUsername = applicantUsername;
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

    public String getApplicantUsername() {
        return applicantUsername;
    }

    public void setApplicantUsername(String applicantUsername) {
        this.applicantUsername = applicantUsername;
    }
}
