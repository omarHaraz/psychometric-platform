package com.psychometric.platform.features.itembank.gcat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gcat_subtests")
public class GcatSubtest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private GcatSubtestCode code;

    @Column(name = "name_ar", nullable = false, length = 255)
    private String nameAr;

    @Column(name = "description_ar", columnDefinition = "TEXT")
    private String descriptionAr;

    @Column(name = "full_mode_quota", nullable = false)
    private int fullModeQuota = 14;

    @Column(name = "quick_mode_quota", nullable = false)
    private int quickModeQuota = 7;

    @Column(name = "time_limit_seconds")
    private Integer timeLimitSeconds;

    public GcatSubtest() {
    }

    public GcatSubtest(GcatSubtestCode code, String nameAr, String descriptionAr, int fullModeQuota, int quickModeQuota, Integer timeLimitSeconds) {
        this.code = code;
        this.nameAr = nameAr;
        this.descriptionAr = descriptionAr;
        this.fullModeQuota = fullModeQuota;
        this.quickModeQuota = quickModeQuota;
        this.timeLimitSeconds = timeLimitSeconds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GcatSubtestCode getCode() {
        return code;
    }

    public void setCode(GcatSubtestCode code) {
        this.code = code;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public int getFullModeQuota() {
        return fullModeQuota;
    }

    public void setFullModeQuota(int fullModeQuota) {
        this.fullModeQuota = fullModeQuota;
    }

    public int getQuickModeQuota() {
        return quickModeQuota;
    }

    public void setQuickModeQuota(int quickModeQuota) {
        this.quickModeQuota = quickModeQuota;
    }

    public Integer getTimeLimitSeconds() {
        return timeLimitSeconds;
    }

    public void setTimeLimitSeconds(Integer timeLimitSeconds) {
        this.timeLimitSeconds = timeLimitSeconds;
    }
}
