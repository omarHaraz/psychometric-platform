package com.psychometric.platform.features.itembank.derailers.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "derailer_type_indicators")
public class DerailerTypeIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "derailer_type_id", nullable = false)
    private DerailerType derailerType;

    @Column(name = "indicator_ar", nullable = false, length = 500)
    private String indicatorAr;

    public DerailerTypeIndicator() {
    }

    public DerailerTypeIndicator(DerailerType derailerType, String indicatorAr) {
        this.derailerType = derailerType;
        this.indicatorAr = indicatorAr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DerailerType getDerailerType() {
        return derailerType;
    }

    public void setDerailerType(DerailerType derailerType) {
        this.derailerType = derailerType;
    }

    public String getIndicatorAr() {
        return indicatorAr;
    }

    public void setIndicatorAr(String indicatorAr) {
        this.indicatorAr = indicatorAr;
    }
}
