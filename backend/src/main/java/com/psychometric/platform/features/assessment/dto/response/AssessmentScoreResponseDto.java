package com.psychometric.platform.features.assessment.dto.response;

import com.psychometric.platform.features.assessment.domain.enums.ReadinessBand;
import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AssessmentScoreResponseDto {

    private Long id;
    private String attemptToken;
    private String candidateName;
    private String candidateEmail;

    private Double personalityScorePct;
    private Double sjtScorePct;
    private Double derailersEffectiveScorePct;
    private Double cognitiveScorePct;

    private Double compositeScore;
    private Double rawCompositeScore;
    private Double validityPenaltyPct;
    private Double cappedPenaltyPct;
    private Integer percentile;
    private ReadinessBand readinessBand;
    private String readinessBandLabelEn;
    private String readinessBandLabelAr;
    private Double socialDesirabilityRiskPct;
    private Boolean elevatedImpressionManagement;
    private Double centralTendencyRatePct;
    private Boolean elevatedCentralTendency;
    private Instant scoredAt;

    private List<TraitScoreDto> traitScores = new ArrayList<>();
    private List<DerailerCategoryScoreDto> derailerCategoryScores = new ArrayList<>();
    private List<GcatSubtestScoreDto> gcatSubtestScores = new ArrayList<>();

    public static class TraitScoreDto {
        private String traitCode;
        private String nameAr;
        private Integer displayOrder;
        private Double rawScore;
        private Double scorePct;

        public TraitScoreDto() {}

        public TraitScoreDto(String traitCode, String nameAr, Integer displayOrder, Double rawScore, Double scorePct) {
            this.traitCode = traitCode;
            this.nameAr = nameAr;
            this.displayOrder = displayOrder;
            this.rawScore = rawScore;
            this.scorePct = scorePct;
        }

        public String getTraitCode() { return traitCode; }
        public void setTraitCode(String traitCode) { this.traitCode = traitCode; }
        public String getNameAr() { return nameAr; }
        public void setNameAr(String nameAr) { this.nameAr = nameAr; }
        public Integer getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
        public Double getRawScore() { return rawScore; }
        public void setRawScore(Double rawScore) { this.rawScore = rawScore; }
        public Double getScorePct() { return scorePct; }
        public void setScorePct(Double scorePct) { this.scorePct = scorePct; }
    }

    public static class DerailerCategoryScoreDto {
        private Long categoryId;
        private String nameAr;
        private Integer displayOrder;
        private Double rawScore;
        private Double scorePct;

        public DerailerCategoryScoreDto() {}

        public DerailerCategoryScoreDto(Long categoryId, String nameAr, Integer displayOrder, Double rawScore, Double scorePct) {
            this.categoryId = categoryId;
            this.nameAr = nameAr;
            this.displayOrder = displayOrder;
            this.rawScore = rawScore;
            this.scorePct = scorePct;
        }

        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
        public String getNameAr() { return nameAr; }
        public void setNameAr(String nameAr) { this.nameAr = nameAr; }
        public Integer getDisplayOrder() { return displayOrder; }
        public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
        public Double getRawScore() { return rawScore; }
        public void setRawScore(Double rawScore) { this.rawScore = rawScore; }
        public Double getScorePct() { return scorePct; }
        public void setScorePct(Double scorePct) { this.scorePct = scorePct; }
    }

    public static class GcatSubtestScoreDto {
        private GcatSubtestCode subtest;
        private Integer correctCount;
        private Integer totalCount = 14;
        private Double scorePct;

        public GcatSubtestScoreDto() {}

        public GcatSubtestScoreDto(GcatSubtestCode subtest, Integer correctCount, Integer totalCount, Double scorePct) {
            this.subtest = subtest;
            this.correctCount = correctCount;
            this.totalCount = totalCount;
            this.scorePct = scorePct;
        }

        public GcatSubtestCode getSubtest() { return subtest; }
        public void setSubtest(GcatSubtestCode subtest) { this.subtest = subtest; }
        public Integer getCorrectCount() { return correctCount; }
        public void setCorrectCount(Integer correctCount) { this.correctCount = correctCount; }
        public Integer getTotalCount() { return totalCount; }
        public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
        public Double getScorePct() { return scorePct; }
        public void setScorePct(Double scorePct) { this.scorePct = scorePct; }
    }

    public static AssessmentScoreResponseDto fromEntity(AssessmentScore score) {
        if (score == null) return null;
        AssessmentScoreResponseDto dto = new AssessmentScoreResponseDto();
        dto.setId(score.getId());
        if (score.getAttempt() != null) {
            dto.setAttemptToken(score.getAttempt().getAttemptToken());
            if (score.getAttempt().getCandidate() != null) {
                dto.setCandidateName(score.getAttempt().getCandidate().getName());
                dto.setCandidateEmail(score.getAttempt().getCandidate().getEmail());
            }
        }
        dto.setPersonalityScorePct(score.getPersonalityScorePct());
        dto.setSjtScorePct(score.getSjtScorePct());
        dto.setDerailersEffectiveScorePct(score.getDerailersEffectiveScorePct());
        dto.setCognitiveScorePct(score.getCognitiveScorePct());
        dto.setCompositeScore(score.getCompositeScore());
        dto.setRawCompositeScore(score.getRawCompositeScore());
        dto.setValidityPenaltyPct(score.getValidityPenaltyPct());
        dto.setCappedPenaltyPct(score.getCappedPenaltyPct());
        dto.setPercentile(score.getPercentile());
        dto.setReadinessBand(score.getReadinessBand());
        if (score.getReadinessBand() != null) {
            dto.setReadinessBandLabelEn(score.getReadinessBand().getLabelEn());
            dto.setReadinessBandLabelAr(score.getReadinessBand().getLabelAr());
        }
        dto.setSocialDesirabilityRiskPct(score.getSocialDesirabilityRiskPct());
        dto.setElevatedImpressionManagement(score.getElevatedImpressionManagement());
        dto.setCentralTendencyRatePct(score.getCentralTendencyRatePct());
        dto.setElevatedCentralTendency(score.getElevatedCentralTendency());
        dto.setScoredAt(score.getScoredAt());

        if (score.getTraitScores() != null) {
            for (var ts : score.getTraitScores()) {
                String code = ts.getTrait() != null ? ts.getTrait().getCode() : "";
                String nameAr = ts.getTrait() != null ? ts.getTrait().getNameAr() : "";
                Integer order = ts.getTrait() != null ? ts.getTrait().getDisplayOrder() : 1;
                dto.getTraitScores().add(new TraitScoreDto(code, nameAr, order, ts.getRawScore(), ts.getScorePct()));
            }
            dto.getTraitScores().sort((a, b) -> Integer.compare(a.getDisplayOrder(), b.getDisplayOrder()));
        }

        if (score.getDerailerCategoryScores() != null) {
            for (var ds : score.getDerailerCategoryScores()) {
                Long catId = ds.getCategory() != null ? ds.getCategory().getId() : null;
                String nameAr = ds.getCategory() != null ? ds.getCategory().getNameAr() : "";
                Integer order = ds.getCategory() != null ? ds.getCategory().getDisplayOrder() : 1;
                dto.getDerailerCategoryScores().add(new DerailerCategoryScoreDto(catId, nameAr, order, ds.getRawScore(), ds.getScorePct()));
            }
            dto.getDerailerCategoryScores().sort((a, b) -> Integer.compare(a.getDisplayOrder(), b.getDisplayOrder()));
        }

        if (score.getGcatSubtestScores() != null) {
            for (var gs : score.getGcatSubtestScores()) {
                dto.getGcatSubtestScores().add(new GcatSubtestScoreDto(gs.getSubtest(), gs.getCorrectCount(), 14, gs.getScorePct()));
            }
        }

        return dto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAttemptToken() { return attemptToken; }
    public void setAttemptToken(String attemptToken) { this.attemptToken = attemptToken; }
    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
    public String getCandidateEmail() { return candidateEmail; }
    public void setCandidateEmail(String candidateEmail) { this.candidateEmail = candidateEmail; }
    public Double getPersonalityScorePct() { return personalityScorePct; }
    public void setPersonalityScorePct(Double personalityScorePct) { this.personalityScorePct = personalityScorePct; }
    public Double getSjtScorePct() { return sjtScorePct; }
    public void setSjtScorePct(Double sjtScorePct) { this.sjtScorePct = sjtScorePct; }
    public Double getDerailersEffectiveScorePct() { return derailersEffectiveScorePct; }
    public void setDerailersEffectiveScorePct(Double derailersEffectiveScorePct) { this.derailersEffectiveScorePct = derailersEffectiveScorePct; }
    public Double getCognitiveScorePct() { return cognitiveScorePct; }
    public void setCognitiveScorePct(Double cognitiveScorePct) { this.cognitiveScorePct = cognitiveScorePct; }
    public Double getCompositeScore() { return compositeScore; }
    public void setCompositeScore(Double compositeScore) { this.compositeScore = compositeScore; }
    public Double getRawCompositeScore() { return rawCompositeScore != null ? rawCompositeScore : compositeScore; }
    public void setRawCompositeScore(Double rawCompositeScore) { this.rawCompositeScore = rawCompositeScore; }
    public Double getValidityPenaltyPct() { return validityPenaltyPct != null ? validityPenaltyPct : 0.0; }
    public void setValidityPenaltyPct(Double validityPenaltyPct) { this.validityPenaltyPct = validityPenaltyPct; }
    public Double getCappedPenaltyPct() { return cappedPenaltyPct != null ? cappedPenaltyPct : 0.0; }
    public void setCappedPenaltyPct(Double cappedPenaltyPct) { this.cappedPenaltyPct = cappedPenaltyPct; }
    public Integer getPercentile() { return percentile; }
    public void setPercentile(Integer percentile) { this.percentile = percentile; }
    public ReadinessBand getReadinessBand() { return readinessBand; }
    public void setReadinessBand(ReadinessBand readinessBand) { this.readinessBand = readinessBand; }
    public String getReadinessBandLabelEn() { return readinessBandLabelEn; }
    public void setReadinessBandLabelEn(String readinessBandLabelEn) { this.readinessBandLabelEn = readinessBandLabelEn; }
    public String getReadinessBandLabelAr() { return readinessBandLabelAr; }
    public void setReadinessBandLabelAr(String readinessBandLabelAr) { this.readinessBandLabelAr = readinessBandLabelAr; }
    public Double getSocialDesirabilityRiskPct() { return socialDesirabilityRiskPct; }
    public void setSocialDesirabilityRiskPct(Double socialDesirabilityRiskPct) { this.socialDesirabilityRiskPct = socialDesirabilityRiskPct; }
    public Boolean getElevatedImpressionManagement() { return elevatedImpressionManagement; }
    public void setElevatedImpressionManagement(Boolean elevatedImpressionManagement) { this.elevatedImpressionManagement = elevatedImpressionManagement; }
    public Double getCentralTendencyRatePct() { return centralTendencyRatePct; }
    public void setCentralTendencyRatePct(Double centralTendencyRatePct) { this.centralTendencyRatePct = centralTendencyRatePct; }
    public Boolean getElevatedCentralTendency() { return elevatedCentralTendency; }
    public void setElevatedCentralTendency(Boolean elevatedCentralTendency) { this.elevatedCentralTendency = elevatedCentralTendency; }
    public Instant getScoredAt() { return scoredAt; }
    public void setScoredAt(Instant scoredAt) { this.scoredAt = scoredAt; }
    public List<TraitScoreDto> getTraitScores() { return traitScores; }
    public void setTraitScores(List<TraitScoreDto> traitScores) { this.traitScores = traitScores; }
    public List<DerailerCategoryScoreDto> getDerailerCategoryScores() { return derailerCategoryScores; }
    public void setDerailerCategoryScores(List<DerailerCategoryScoreDto> derailerCategoryScores) { this.derailerCategoryScores = derailerCategoryScores; }
    public List<GcatSubtestScoreDto> getGcatSubtestScores() { return gcatSubtestScores; }
    public void setGcatSubtestScores(List<GcatSubtestScoreDto> gcatSubtestScores) { this.gcatSubtestScores = gcatSubtestScores; }
}
