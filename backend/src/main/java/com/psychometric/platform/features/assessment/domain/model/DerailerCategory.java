package com.psychometric.platform.features.assessment.domain.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "derailer_categories")
public class DerailerCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ar", nullable = false, length = 255)
    private String nameAr;

    @Column(name = "definition_ar", columnDefinition = "TEXT")
    private String definitionAr;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "derailer_category_indicators", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "indicator_ar", columnDefinition = "TEXT")
    private List<String> indicatorsAr = new ArrayList<>();

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 1;

    public DerailerCategory() {
    }

    public DerailerCategory(String nameAr, String definitionAr, List<String> indicatorsAr, Integer displayOrder) {
        this.nameAr = nameAr;
        this.definitionAr = definitionAr;
        this.indicatorsAr = indicatorsAr != null ? indicatorsAr : new ArrayList<>();
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<String> getIndicatorsAr() {
        return indicatorsAr;
    }

    public void setIndicatorsAr(List<String> indicatorsAr) {
        this.indicatorsAr = indicatorsAr;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
