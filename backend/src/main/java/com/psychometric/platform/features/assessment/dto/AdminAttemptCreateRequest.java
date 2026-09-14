package com.psychometric.platform.features.assessment.dto;

public class AdminAttemptCreateRequest {
    private Long candidateId;
    private String examType = "PSYCHOMETRIC";

    public Long getCandidateId() { return candidateId; }
    public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
    public String getExamType() { return examType != null ? examType : "PSYCHOMETRIC"; }
    public void setExamType(String examType) { this.examType = (examType != null && !examType.isBlank()) ? examType.toUpperCase() : "PSYCHOMETRIC"; }
}
