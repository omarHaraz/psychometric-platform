package com.psychometric.platform.features.report.dto;

import com.psychometric.platform.features.assessment.domain.enums.ReadinessBand;
import org.thymeleaf.context.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmploymentReportDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // Candidate & Exam Metadata
    private String candidateName;
    private String candidateId;
    private String referenceNumber;
    private String reportDate;
    private String attemptToken;
    private String examTitle;
    private String reportTitle;

    // Overall Composite & Readiness
    private Double compositeScore;
    private Double rawCompositeScore;
    private Double validityPenaltyPct;
    private Double cappedPenaltyPct;
    private Double socialDesirabilityRiskPct;
    private Boolean elevatedImpressionManagement;
    private Double centralTendencyRatePct;
    private Boolean elevatedCentralTendency;
    private ReadinessBand readinessBand;
    private String readinessBandLabelAr;
    private String readinessBandLabelEn;
    private int caseNumber; // 1, 2, 3, 4
    private String diagnosticCaseTitle;
    private String diagnosticCaseSubtitle;

    // Battery 1: Personality & Workplace Traits (PQ10) - 30%
    private Double personalityScorePct;
    private String personalityStatus; // متقدم / متوسط / يحتاج تطوير

    // Battery 2: Situational Judgment Test (SJT) - 30%
    private Double sjtScorePct;
    private String sjtStatus;

    // Battery 3: Cognitive Abilities Test (GCAT) - 40%
    private Double cognitiveScorePct;
    private String cognitiveStatus;

    // GCAT Subtests
    private Double verbalScorePct;
    private String verbalStatus;

    private Double numericalScorePct;
    private String numericalStatus;

    private Double abstractScorePct;
    private String abstractStatus;

    // Dynamic Highlights & Diagnostics
    private String highestComponentName;
    private String lowestComponentName;
    private List<String> deficientComponents = new ArrayList<>();

    // Report Sections Narrative
    private List<DiagnosticPoint> diagnosticPoints = new ArrayList<>();
    private String actionPlanTitle;
    private List<ActionPlanItem> actionPlanItems = new ArrayList<>();

    // Competencies Breakdown (12 Employment Competencies)
    private List<EmploymentCompetencyScore> competencyScores = new ArrayList<>();

    public EmploymentReportDto() {
    }

    public static class DiagnosticPoint implements Serializable {
        private String category;
        private String narrative;

        public DiagnosticPoint() {}
        public DiagnosticPoint(String category, String narrative) {
            this.category = category;
            this.narrative = narrative;
        }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getNarrative() { return narrative; }
        public void setNarrative(String narrative) { this.narrative = narrative; }
    }

    public static class ActionPlanItem implements Serializable {
        private String stepNumber;
        private String title;
        private String description;
        private String badge;

        public ActionPlanItem() {}
        public ActionPlanItem(String stepNumber, String title, String description, String badge) {
            this.stepNumber = stepNumber;
            this.title = title;
            this.description = description;
            this.badge = badge;
        }
        public String getStepNumber() { return stepNumber; }
        public void setStepNumber(String stepNumber) { this.stepNumber = stepNumber; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getBadge() { return badge; }
        public void setBadge(String badge) { this.badge = badge; }
    }

    public static class EmploymentCompetencyScore implements Serializable {
        private String nameAr;
        private String code;
        private Double scorePct;
        private String status; // متقدم / متوسط / يحتاج تطوير
        private String statusColor;

        public EmploymentCompetencyScore() {}
        public EmploymentCompetencyScore(String nameAr, String code, Double scorePct, String status, String statusColor) {
            this.nameAr = nameAr;
            this.code = code;
            this.scorePct = scorePct;
            this.status = status;
            this.statusColor = statusColor;
        }
        public String getNameAr() { return nameAr; }
        public void setNameAr(String nameAr) { this.nameAr = nameAr; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public Double getScorePct() { return scorePct; }
        public void setScorePct(Double scorePct) { this.scorePct = scorePct; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getStatusColor() { return statusColor; }
        public void setStatusColor(String statusColor) { this.statusColor = statusColor; }
    }

    // Getters and Setters
    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getCandidateId() { return candidateId; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

    public String getReferenceNumber() { return referenceNumber; }
    public void setReferenceNumber(String referenceNumber) { this.referenceNumber = referenceNumber; }

    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }

    public String getAttemptToken() { return attemptToken; }
    public void setAttemptToken(String attemptToken) { this.attemptToken = attemptToken; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getReportTitle() { return reportTitle; }
    public void setReportTitle(String reportTitle) { this.reportTitle = reportTitle; }

    public Double getCompositeScore() { return compositeScore; }
    public void setCompositeScore(Double compositeScore) { this.compositeScore = compositeScore; }

    public ReadinessBand getReadinessBand() { return readinessBand; }
    public void setReadinessBand(ReadinessBand readinessBand) { this.readinessBand = readinessBand; }

    public String getReadinessBandLabelAr() { return readinessBandLabelAr; }
    public void setReadinessBandLabelAr(String readinessBandLabelAr) { this.readinessBandLabelAr = readinessBandLabelAr; }

    public String getReadinessBandLabelEn() { return readinessBandLabelEn; }
    public void setReadinessBandLabelEn(String readinessBandLabelEn) { this.readinessBandLabelEn = readinessBandLabelEn; }

    public int getCaseNumber() { return caseNumber; }
    public void setCaseNumber(int caseNumber) { this.caseNumber = caseNumber; }

    public String getDiagnosticCaseTitle() { return diagnosticCaseTitle; }
    public void setDiagnosticCaseTitle(String diagnosticCaseTitle) { this.diagnosticCaseTitle = diagnosticCaseTitle; }

    public String getDiagnosticCaseSubtitle() { return diagnosticCaseSubtitle; }
    public void setDiagnosticCaseSubtitle(String diagnosticCaseSubtitle) { this.diagnosticCaseSubtitle = diagnosticCaseSubtitle; }

    public Double getPersonalityScorePct() { return personalityScorePct; }
    public void setPersonalityScorePct(Double personalityScorePct) { this.personalityScorePct = personalityScorePct; }

    public String getPersonalityStatus() { return personalityStatus; }
    public void setPersonalityStatus(String personalityStatus) { this.personalityStatus = personalityStatus; }

    public Double getSjtScorePct() { return sjtScorePct; }
    public void setSjtScorePct(Double sjtScorePct) { this.sjtScorePct = sjtScorePct; }

    public String getSjtStatus() { return sjtStatus; }
    public void setSjtStatus(String sjtStatus) { this.sjtStatus = sjtStatus; }

    public Double getCognitiveScorePct() { return cognitiveScorePct; }
    public void setCognitiveScorePct(Double cognitiveScorePct) { this.cognitiveScorePct = cognitiveScorePct; }

    public String getCognitiveStatus() { return cognitiveStatus; }
    public void setCognitiveStatus(String cognitiveStatus) { this.cognitiveStatus = cognitiveStatus; }

    public Double getVerbalScorePct() { return verbalScorePct; }
    public void setVerbalScorePct(Double verbalScorePct) { this.verbalScorePct = verbalScorePct; }

    public String getVerbalStatus() { return verbalStatus; }
    public void setVerbalStatus(String verbalStatus) { this.verbalStatus = verbalStatus; }

    public Double getNumericalScorePct() { return numericalScorePct; }
    public void setNumericalScorePct(Double numericalScorePct) { this.numericalScorePct = numericalScorePct; }

    public String getNumericalStatus() { return numericalStatus; }
    public void setNumericalStatus(String numericalStatus) { this.numericalStatus = numericalStatus; }

    public Double getAbstractScorePct() { return abstractScorePct; }
    public void setAbstractScorePct(Double abstractScorePct) { this.abstractScorePct = abstractScorePct; }

    public String getAbstractStatus() { return abstractStatus; }
    public void setAbstractStatus(String abstractStatus) { this.abstractStatus = abstractStatus; }

    public String getHighestComponentName() { return highestComponentName; }
    public void setHighestComponentName(String highestComponentName) { this.highestComponentName = highestComponentName; }

    public String getLowestComponentName() { return lowestComponentName; }
    public void setLowestComponentName(String lowestComponentName) { this.lowestComponentName = lowestComponentName; }

    public List<String> getDeficientComponents() { return deficientComponents; }
    public void setDeficientComponents(List<String> deficientComponents) { this.deficientComponents = deficientComponents; }

    public List<DiagnosticPoint> getDiagnosticPoints() { return diagnosticPoints; }
    public void setDiagnosticPoints(List<DiagnosticPoint> diagnosticPoints) { this.diagnosticPoints = diagnosticPoints; }

    public String getActionPlanTitle() { return actionPlanTitle; }
    public void setActionPlanTitle(String actionPlanTitle) { this.actionPlanTitle = actionPlanTitle; }

    public List<ActionPlanItem> getActionPlanItems() { return actionPlanItems; }
    public void setActionPlanItems(List<ActionPlanItem> actionPlanItems) { this.actionPlanItems = actionPlanItems; }

    public List<EmploymentCompetencyScore> getCompetencyScores() { return competencyScores; }
    public void setCompetencyScores(List<EmploymentCompetencyScore> competencyScores) { this.competencyScores = competencyScores; }

    public Context toThymeleafContext() {
        Context context = new Context();
        context.setVariable("candidateName", this.candidateName);
        context.setVariable("candidateId", this.candidateId);
        context.setVariable("referenceNumber", this.referenceNumber);
        context.setVariable("reportDate", this.reportDate);
        context.setVariable("attemptToken", this.attemptToken);
        context.setVariable("examTitle", this.examTitle);
        context.setVariable("reportTitle", this.reportTitle);

        context.setVariable("compositeScore", this.compositeScore);
        context.setVariable("readinessBand", this.readinessBand);
        context.setVariable("readinessBandLabelAr", this.readinessBandLabelAr);
        context.setVariable("readinessBandLabelEn", this.readinessBandLabelEn);
        context.setVariable("caseNumber", this.caseNumber);
        context.setVariable("diagnosticCaseTitle", this.diagnosticCaseTitle);
        context.setVariable("diagnosticCaseSubtitle", this.diagnosticCaseSubtitle);

        context.setVariable("personalityScorePct", this.personalityScorePct);
        context.setVariable("personalityStatus", this.personalityStatus);

        context.setVariable("sjtScorePct", this.sjtScorePct);
        context.setVariable("sjtStatus", this.sjtStatus);

        context.setVariable("cognitiveScorePct", this.cognitiveScorePct);
        context.setVariable("cognitiveStatus", this.cognitiveStatus);

        context.setVariable("verbalScorePct", this.verbalScorePct);
        context.setVariable("verbalStatus", this.verbalStatus);

        context.setVariable("numericalScorePct", this.numericalScorePct);
        context.setVariable("numericalStatus", this.numericalStatus);

        context.setVariable("abstractScorePct", this.abstractScorePct);
        context.setVariable("abstractStatus", this.abstractStatus);

        context.setVariable("highestComponentName", this.highestComponentName);
        context.setVariable("lowestComponentName", this.lowestComponentName);
        context.setVariable("deficientComponents", this.deficientComponents);

        context.setVariable("diagnosticPoints", this.diagnosticPoints);
        context.setVariable("actionPlanTitle", this.actionPlanTitle);
        context.setVariable("actionPlanItems", this.actionPlanItems);
        context.setVariable("competencyScores", this.competencyScores);
        context.setVariable("rawCompositeScore", this.rawCompositeScore);
        context.setVariable("validityPenaltyPct", this.validityPenaltyPct);
        context.setVariable("cappedPenaltyPct", this.cappedPenaltyPct);
        context.setVariable("socialDesirabilityRiskPct", this.socialDesirabilityRiskPct);
        context.setVariable("elevatedImpressionManagement", this.elevatedImpressionManagement);
        context.setVariable("centralTendencyRatePct", this.centralTendencyRatePct);
        context.setVariable("elevatedCentralTendency", this.elevatedCentralTendency);

        return context;
    }

    public Double getRawCompositeScore() { return rawCompositeScore; }
    public void setRawCompositeScore(Double rawCompositeScore) { this.rawCompositeScore = rawCompositeScore; }

    public Double getValidityPenaltyPct() { return validityPenaltyPct; }
    public void setValidityPenaltyPct(Double validityPenaltyPct) { this.validityPenaltyPct = validityPenaltyPct; }

    public Double getCappedPenaltyPct() { return cappedPenaltyPct; }
    public void setCappedPenaltyPct(Double cappedPenaltyPct) { this.cappedPenaltyPct = cappedPenaltyPct; }

    public Double getSocialDesirabilityRiskPct() { return socialDesirabilityRiskPct; }
    public void setSocialDesirabilityRiskPct(Double socialDesirabilityRiskPct) { this.socialDesirabilityRiskPct = socialDesirabilityRiskPct; }

    public Boolean getElevatedImpressionManagement() { return elevatedImpressionManagement; }
    public void setElevatedImpressionManagement(Boolean elevatedImpressionManagement) { this.elevatedImpressionManagement = elevatedImpressionManagement; }

    public Double getCentralTendencyRatePct() { return centralTendencyRatePct; }
    public void setCentralTendencyRatePct(Double centralTendencyRatePct) { this.centralTendencyRatePct = centralTendencyRatePct; }

    public Boolean getElevatedCentralTendency() { return elevatedCentralTendency; }
    public void setElevatedCentralTendency(Boolean elevatedCentralTendency) { this.elevatedCentralTendency = elevatedCentralTendency; }
}
