package com.example.mortgage.dto;

import java.math.BigDecimal;

public class MortgageApplicationRequest {
    private String applicantName;
    private Double amount;

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

}
