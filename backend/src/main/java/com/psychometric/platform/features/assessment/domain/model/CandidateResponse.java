package com.psychometric.platform.features.assessment.domain.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "candidate_responses")
public class CandidateResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private BatterySession batterySession;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    private Integer selectedLikert;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private List<String> rankingOrder;

    private String selectedOption;

    private Long responseTimeMs;
    private Instant submittedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BatterySession getBatterySession() { return batterySession; }
    public void setBatterySession(BatterySession batterySession) { this.batterySession = batterySession; }
    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }
    public Integer getSelectedLikert() { return selectedLikert; }
    public void setSelectedLikert(Integer selectedLikert) { this.selectedLikert = selectedLikert; }
    public List<String> getRankingOrder() { return rankingOrder; }
    public void setRankingOrder(List<String> rankingOrder) { this.rankingOrder = rankingOrder; }
    public String getSelectedOption() { return selectedOption; }
    public void setSelectedOption(String selectedOption) { this.selectedOption = selectedOption; }
    public Long getResponseTimeMs() { return responseTimeMs; }
    public void setResponseTimeMs(Long responseTimeMs) { this.responseTimeMs = responseTimeMs; }
    public Instant getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Instant submittedAt) { this.submittedAt = submittedAt; }
}
