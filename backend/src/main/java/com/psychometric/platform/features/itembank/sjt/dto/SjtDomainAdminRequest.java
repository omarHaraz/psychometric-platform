package com.psychometric.platform.features.itembank.sjt.dto;

import jakarta.validation.constraints.NotBlank;

public class SjtDomainAdminRequest {

    @NotBlank(message = "رمز المجال مطلوب")
    private String code;

    @NotBlank(message = "اسم المجال مطلوب")
    private String nameAr;

    private String descriptionAr;

    private int displayOrder = 0;

    public SjtDomainAdminRequest() {
    }

    public SjtDomainAdminRequest(String code, String nameAr, String descriptionAr, int displayOrder) {
        this.code = code;
        this.nameAr = nameAr;
        this.descriptionAr = descriptionAr;
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

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
