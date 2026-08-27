package com.psychometric.platform.features.itembank.gcat.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "gcat_options")
public class GcatOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private GcatQuestion question;

    @Enumerated(EnumType.STRING)
    @Column(name = "option_key", nullable = false, length = 10)
    private GcatOptionKey optionKey;

    @Column(name = "option_text_ar", columnDefinition = "TEXT")
    private String optionTextAr;

    @Column(name = "option_image_url", length = 500)
    private String optionImageUrl;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 1;

    @Column(name = "is_correct", nullable = false)
    private boolean correct = false;

    public GcatOption() {
    }

    public GcatOption(GcatQuestion question, GcatOptionKey optionKey, String optionTextAr, String optionImageUrl, boolean correct) {
        this.question = question;
        this.optionKey = optionKey;
        this.optionTextAr = optionTextAr;
        this.optionImageUrl = optionImageUrl;
        this.correct = correct;
        this.displayOrder = optionKey != null ? optionKey.ordinal() + 1 : 1;
    }

    public GcatOption(GcatQuestion question, GcatOptionKey optionKey, String optionTextAr, String optionImageUrl, boolean correct, Integer displayOrder) {
        this.question = question;
        this.optionKey = optionKey;
        this.optionTextAr = optionTextAr;
        this.optionImageUrl = optionImageUrl;
        this.correct = correct;
        this.displayOrder = displayOrder != null ? displayOrder : (optionKey != null ? optionKey.ordinal() + 1 : 1);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GcatQuestion getQuestion() {
        return question;
    }

    public void setQuestion(GcatQuestion question) {
        this.question = question;
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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
