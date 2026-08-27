package com.psychometric.platform.features.itembank.derailers.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "derailer_types")
public class DerailerType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ar", nullable = false, length = 255)
    private String nameAr;

    @Column(name = "definition_ar", columnDefinition = "TEXT")
    private String definitionAr;

    @OneToMany(mappedBy = "derailerType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DerailerTypeIndicator> indicators = new ArrayList<>();

    public DerailerType() {
    }

    public DerailerType(String nameAr, String definitionAr) {
        this.nameAr = nameAr;
        this.definitionAr = definitionAr;
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

    public List<DerailerTypeIndicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<DerailerTypeIndicator> indicators) {
        this.indicators = indicators;
    }
}
