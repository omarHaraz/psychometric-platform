package com.psychometric.platform.features.itembank.sjt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sjt_domains")
public class SjtDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "name_ar", nullable = false, length = 255)
    private String nameAr;

    @Column(name = "description_ar", columnDefinition = "TEXT")
    private String descriptionAr;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    @Column(name = "default_quota", nullable = false)
    private int defaultQuota = 0;

    public SjtDomain() {
    }

    public SjtDomain(String code, String nameAr, String descriptionAr, int displayOrder) {
        this.code = code;
        this.nameAr = nameAr;
        this.descriptionAr = descriptionAr;
        this.displayOrder = displayOrder;
        this.defaultQuota = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getDefaultQuota() {
        return defaultQuota;
    }

    public void setDefaultQuota(int defaultQuota) {
        this.defaultQuota = defaultQuota;
    }
}
