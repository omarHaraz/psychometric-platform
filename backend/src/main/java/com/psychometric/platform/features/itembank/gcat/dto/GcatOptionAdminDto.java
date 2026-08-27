package com.psychometric.platform.features.itembank.gcat.dto;

import com.psychometric.platform.features.itembank.gcat.entity.GcatOptionKey;
import jakarta.validation.constraints.NotNull;

public class GcatOptionAdminDto {
    private Long id;

    @NotNull(message = "مفتاح الخيار مطلوب")
    private GcatOptionKey optionKey;

    private String optionTextAr;
    private String optionImageUrl;
    private boolean correct = false;

    public GcatOptionAdminDto() {
    }

    public GcatOptionAdminDto(Long id, GcatOptionKey optionKey, String optionTextAr, String optionImageUrl, boolean correct) {
        this.id = id;
        this.optionKey = optionKey;
        this.optionTextAr = optionTextAr;
        this.optionImageUrl = optionImageUrl;
        this.correct = correct;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GcatOptionKey getOptionKey() {
        return optionKey;
    }

    public void setOptionKey(GcatOptionKey optionKey) {
        this.optionKey = optionKey;
    }

    public String getOptionTextAr() {
        return optionTextAr;
    }

    public void setOptionTextAr(String optionTextAr) {
        this.optionTextAr = optionTextAr;
    }

    public String getOptionImageUrl() {
        return optionImageUrl;
    }

    public void setOptionImageUrl(String optionImageUrl) {
        this.optionImageUrl = optionImageUrl;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
