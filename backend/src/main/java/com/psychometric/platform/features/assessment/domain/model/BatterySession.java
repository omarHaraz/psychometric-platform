package com.psychometric.platform.features.assessment.domain.model;

import com.psychometric.platform.features.assessment.domain.enums.BatteryType;
import com.psychometric.platform.features.assessment.domain.enums.SessionState;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "battery_sessions")
public class BatterySession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private AssessmentAttempt attempt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatteryType batteryType;

    @Column(nullable = false, updatable = false)
    private Integer sequenceOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionState state;

    private Instant startTime;
    private Instant submitTime;

    @Column(nullable = false)
    private Integer timeLimitSeconds;

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToMany(mappedBy = "batterySession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidateResponse> responses = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "battery_session_sampled_items", joinColumns = @JoinColumn(name = "session_id"))
    @OrderColumn(name = "position")
    @Column(name = "item_id")
    private List<Long> sampledItemIds = new ArrayList<>();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public AssessmentAttempt getAttempt() { return attempt; }
    public void setAttempt(AssessmentAttempt attempt) { this.attempt = attempt; }
    public BatteryType getBatteryType() { return batteryType; }
    public void setBatteryType(BatteryType batteryType) { this.batteryType = batteryType; }
    public Integer getSequenceOrder() { return sequenceOrder; }
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }
    public SessionState getState() { return state; }
    public void setState(SessionState state) { this.state = state; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    public Instant getSubmitTime() { return submitTime; }
    public void setSubmitTime(Instant submitTime) { this.submitTime = submitTime; }
    public Integer getTimeLimitSeconds() { return timeLimitSeconds; }
    public void setTimeLimitSeconds(Integer timeLimitSeconds) { this.timeLimitSeconds = timeLimitSeconds; }
    public List<CandidateResponse> getResponses() { return responses; }
    public void setResponses(List<CandidateResponse> responses) { this.responses = responses; }
    public List<Long> getSampledItemIds() { return sampledItemIds; }
    public void setSampledItemIds(List<Long> sampledItemIds) { this.sampledItemIds = sampledItemIds; }
}
