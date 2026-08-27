package com.psychometric.platform.features.itembank.personality.dto;

import jakarta.validation.constraints.NotBlank;

public class CompetencyAdminRequest {

    @NotBlank(message = "رمز الكفاءة مطلوب")
    private String code;

    @NotBlank(message = "اسم الكفاءة مطلوب")
    private String nameAr;

    private String definitionAr;

    private int displayOrder = 0;

    public CompetencyAdminRequest() {
    }

    public CompetencyAdminRequest(String code, String nameAr, String definitionAr, int displayOrder) {
        this.code = code;
        this.nameAr = nameAr;
        this.definitionAr = definitionAr;
        this.displayOrder = displayOrder;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
