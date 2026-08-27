package com.psychometric.platform.features.itembank.derailers.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class DerailerTypeAdminRequest {

    @NotBlank(message = "اسم نمط السلوك المعطل مطلوب")
    private String nameAr;

    private String definitionAr;

    private List<String> indicators;

    public DerailerTypeAdminRequest() {
    }

    public DerailerTypeAdminRequest(String nameAr, String definitionAr, List<String> indicators) {
        this.nameAr = nameAr;
        this.definitionAr = definitionAr;
        this.indicators = indicators;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getDefinitionAr() {
        return definitionAr;
    }

    public void setDefinitionAr(String definitionAr) {
        this.definitionAr = definitionAr;
    }

    public List<String> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<String> indicators) {
        this.indicators = indicators;
    }
}
