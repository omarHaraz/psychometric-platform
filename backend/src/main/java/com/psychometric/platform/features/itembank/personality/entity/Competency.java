package com.psychometric.platform.features.itembank.personality.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "competencies")
public class Competency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "name_ar", nullable = false, length = 255)
    private String nameAr;

    @Column(name = "definition_ar", columnDefinition = "TEXT")
    private String definitionAr;

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    public Competency() {
    }

    public Competency(String code, String nameAr, String definitionAr, int displayOrder) {
        this.code = code;
        this.nameAr = nameAr;
        this.definitionAr = definitionAr;
        this.displayOrder = displayOrder;
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
