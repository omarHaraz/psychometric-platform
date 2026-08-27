package com.psychometric.platform.features.itembank.gcat.dto;

import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class GcatSubtestAdminRequest {

    @NotNull(message = "رمز الاختبار الفرعي مطلوب")
    private GcatSubtestCode code;

    @NotBlank(message = "اسم الاختبار الفرعي مطلوب")
    private String nameAr;

    private String descriptionAr;

    private int fullModeQuota = 14;
    private int quickModeQuota = 7;
    private Integer timeLimitSeconds;

    public GcatSubtestAdminRequest() {
    }

    public GcatSubtestAdminRequest(GcatSubtestCode code, String nameAr, String descriptionAr, int fullModeQuota, int quickModeQuota, Integer timeLimitSeconds) {
        this.code = code;
        this.nameAr = nameAr;
        this.descriptionAr = descriptionAr;
        this.fullModeQuota = fullModeQuota;
        this.quickModeQuota = quickModeQuota;
        this.timeLimitSeconds = timeLimitSeconds;
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
