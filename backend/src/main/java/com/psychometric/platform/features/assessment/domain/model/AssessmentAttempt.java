package com.psychometric.platform.features.assessment.domain.model;

import com.psychometric.platform.features.assessment.domain.enums.AttemptState;
import com.psychometric.platform.features.user.entity.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessment_attempts")
public class AssessmentAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String attemptToken;

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"password", "roles", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @com.fasterxml.jackson.annotation.JsonIgnoreProperties({"password", "roles", "hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id", nullable = false)
    private User createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttemptState state;

    @Column(nullable = false)
    private Integer currentBatteryIndex;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant startTime;
    private Instant submitTime;

    @OneToMany(mappedBy = "attempt", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("sequenceOrder ASC")
    private List<BatterySession> batterySessions = new ArrayList<>();

    @com.fasterxml.jackson.annotation.JsonIgnore
    @OneToOne(mappedBy = "attempt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AssessmentScore score;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAttemptToken() { return attemptToken; }
    public void setAttemptToken(String attemptToken) { this.attemptToken = attemptToken; }
    public User getCandidate() { return candidate; }
    public void setCandidate(User candidate) { this.candidate = candidate; }
    public User getCreatedBy() { return createdBy; }
    public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
    public AttemptState getState() { return state; }
    public void setState(AttemptState state) { this.state = state; }
    public Integer getCurrentBatteryIndex() { return currentBatteryIndex; }
    public void setCurrentBatteryIndex(Integer currentBatteryIndex) { this.currentBatteryIndex = currentBatteryIndex; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    public Instant getSubmitTime() { return submitTime; }
    public void setSubmitTime(Instant submitTime) { this.submitTime = submitTime; }
    public List<BatterySession> getBatterySessions() { return batterySessions; }
    public void setBatterySessions(List<BatterySession> batterySessions) { this.batterySessions = batterySessions; }
    public AssessmentScore getScore() { return score; }
    public void setScore(AssessmentScore score) { this.score = score; }
}
