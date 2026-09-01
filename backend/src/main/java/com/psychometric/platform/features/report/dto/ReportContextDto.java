package com.psychometric.platform.features.report.dto;

import org.springframework.ui.Model;
import org.thymeleaf.context.Context;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Comprehensive master Data Transfer Object (Context Variable Dictionary)
 * for the entire 15-page Leadership Assessment Report.
 *
 * All Thymeleaf templates under {@code src/main/resources/templates/report/} bind
 * directly to the standardized variable names defined in this DTO.
 */
public class ReportContextDto implements Serializable {

    private static final long serialVersionUID = 1L;

    // =========================================================================
    // Global & Shared Variables
    // =========================================================================
    private String candidateId = "PCIV126371";
    private String candidateName;
    private String reportDate = LocalDate.now().toString();
    private String evaluationPurpose = "تقرير الكفاءات للقادة: تطوير";
    private String companyLogoBase64 = "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 120 40'><circle cx='20' cy='20' r='12' fill='%231e3a4c'/><circle cx='20' cy='20' r='5' fill='%237a4b3a'/></svg>";
    private String logoUrl;

    // =========================================================================
    // Page 1: Cover Page (cover-page.html)
    // =========================================================================
    private String resultScore = "88.5";

    // =========================================================================
    // Page 2: Introduction & Impression Management (page2.html / page-2-intro.html)
    // =========================================================================
    private Integer socialScore = 5;
    private String socialRisk = "متوسط";
    private String socialInterpretation = null;

    private Integer centralScore = 2;
    private String centralRisk = "منخفض";
    private String centralInterpretation = null;

    // =========================================================================
    // Page 3: Framework Definitions (page3.html / page-3-framework.html)
    // Static content - no dynamic variables required.
    // =========================================================================

    // =========================================================================
    // Page 4: Results Summary – Personality Traits 1–10 (page4.html / page-4-summary-traits.html)
    // =========================================================================
    private Integer reservedScore = 6;
    private String reservedText = "تشير هذه النتيجة إلى متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة التحفظ.";

    private Integer emotionalityScore = 5;
    private String emotionalityText = "تشير هذه النتيجة إلى متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة الانفعالية.";

    private Integer hostilityScore = 6;
    private String hostilityText = "تشير هذه النتيجة إلى متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة العدائية.";

    private Integer impulsivityScore = 6;
    private String impulsivityText = "تشير هذه النتيجة إلى متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة الاندفاعية.";

    private Integer rigidityScore = 6;
    private String rigidityText = "متوسط احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة الصرامة.";

    private Integer unconventionalityScore = 7;
    private String unconventionalityText = "تشير هذه النتيجة إلى متوسط إلى مرتفع احتمال إظهار سلوكيات مُقيّدة مرتبطة بسمة اللامألوفية.";

    // =========================================================================
    // Page 5: Results Summary – Competencies & General Abilities (page5.html / page-5-summary-competencies.html)
    // =========================================================================
    // Behavioral Competencies (Score: 1–5, Color: HEX)
    private Integer overallScore = 4;
    private String overallColor = "#c87d46";

    private Double commScore = 4.0;
    private String commColor = "#c87d46";

    private Double initiativeScore = 4.0;
    private String initiativeColor = "#c87d46";

    private Double decisionScore = 4.0;
    private String decisionColor = "#c87d46";

    private Double leadershipScore = 4.0;
    private String leadershipColor = "#c87d46";

    private Double strategicScore = 2.0;
    private String strategicColor = "#d32f2f";

    private Double skillsScore = 2.0;
    private String skillsColor = "#d32f2f";

    private Double adaptabilityScore = 4.0;
    private String adaptabilityColor = "#c87d46";

    private Double analysisScore = 5.0;
    private String analysisColor = "#388e3c";

    // General Abilities (Score: 1–5, Color: HEX)
    private String generalAbilitiesColor = "#c87d46";

    private Double abstractScore = 4.0;
    private String abstractColor = "#c87d46";

    private Double numericalScore = 4.0;
    private String numericalColor = "#c87d46";

    private Double verbalScore = 2.0;
    private String verbalColor = "#d32f2f";

    // =========================================================================
    // Pages 7 to 14: Detailed Competency Breakdowns (competency-detail.html)
    // =========================================================================
    private Map<Integer, CompetencyDetailDto> competencyPages = new HashMap<>();

    // =========================================================================
    // Page 15: Development Plan – GROW Model (page15.html / page-15-grow-plan.html)
    // =========================================================================
    private String growGoalText = "";
    private String growRealityText = "";
    private String growOptionsText = "";
    private String growWillText = "";

    // =========================================================================
    // Constructors
    // =========================================================================
    public ReportContextDto() {
    }

    // =========================================================================
    // Getters and Setters
    // =========================================================================
    public String getCandidateId() { return candidateId; }
    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

    public String getCandidateName() { return candidateName; }
    public void setCandidateName(String candidateName) { this.candidateName = candidateName; }

    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }

    public String getEvaluationPurpose() { return evaluationPurpose; }
    public void setEvaluationPurpose(String evaluationPurpose) { this.evaluationPurpose = evaluationPurpose; }

    public String getCompanyLogoBase64() { return companyLogoBase64; }
    public void setCompanyLogoBase64(String companyLogoBase64) { this.companyLogoBase64 = companyLogoBase64; }

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }

    public String getResultScore() { return resultScore; }
    public void setResultScore(String resultScore) { this.resultScore = resultScore; }

    public Integer getSocialScore() { return socialScore; }
    public void setSocialScore(Integer socialScore) { this.socialScore = socialScore; }

    public String getSocialRisk() { return socialRisk; }
    public void setSocialRisk(String socialRisk) { this.socialRisk = socialRisk; }

    public String getSocialInterpretation() { return socialInterpretation; }
    public void setSocialInterpretation(String socialInterpretation) { this.socialInterpretation = socialInterpretation; }

    public Integer getCentralScore() { return centralScore; }
    public void setCentralScore(Integer centralScore) { this.centralScore = centralScore; }

    public String getCentralRisk() { return centralRisk; }
    public void setCentralRisk(String centralRisk) { this.centralRisk = centralRisk; }

    public String getCentralInterpretation() { return centralInterpretation; }
    public void setCentralInterpretation(String centralInterpretation) { this.centralInterpretation = centralInterpretation; }

    public Integer getReservedScore() { return reservedScore; }
    public void setReservedScore(Integer reservedScore) { this.reservedScore = reservedScore; }

    public String getReservedText() { return reservedText; }
    public void setReservedText(String reservedText) { this.reservedText = reservedText; }

    public Integer getEmotionalityScore() { return emotionalityScore; }
    public void setEmotionalityScore(Integer emotionalityScore) { this.emotionalityScore = emotionalityScore; }

    public String getEmotionalityText() { return emotionalityText; }
    public void setEmotionalityText(String emotionalityText) { this.emotionalityText = emotionalityText; }

    public Integer getHostilityScore() { return hostilityScore; }
    public void setHostilityScore(Integer hostilityScore) { this.hostilityScore = hostilityScore; }

    public String getHostilityText() { return hostilityText; }
    public void setHostilityText(String hostilityText) { this.hostilityText = hostilityText; }

    public Integer getImpulsivityScore() { return impulsivityScore; }
    public void setImpulsivityScore(Integer impulsivityScore) { this.impulsivityScore = impulsivityScore; }

    public String getImpulsivityText() { return impulsivityText; }
    public void setImpulsivityText(String impulsivityText) { this.impulsivityText = impulsivityText; }

    public Integer getRigidityScore() { return rigidityScore; }
    public void setRigidityScore(Integer rigidityScore) { this.rigidityScore = rigidityScore; }

    public String getRigidityText() { return rigidityText; }
    public void setRigidityText(String rigidityText) { this.rigidityText = rigidityText; }

    public Integer getUnconventionalityScore() { return unconventionalityScore; }
    public void setUnconventionalityScore(Integer unconventionalityScore) { this.unconventionalityScore = unconventionalityScore; }

    public String getUnconventionalityText() { return unconventionalityText; }
    public void setUnconventionalityText(String unconventionalityText) { this.unconventionalityText = unconventionalityText; }

    public Integer getOverallScore() { return overallScore; }
    public void setOverallScore(Integer overallScore) { this.overallScore = overallScore; }

    public String getOverallColor() { return overallColor; }
    public void setOverallColor(String overallColor) { this.overallColor = overallColor; }

    public Double getCommScore() { return commScore; }
    public void setCommScore(Double commScore) { this.commScore = commScore; }
    public void setCommScore(Integer commScore) { this.commScore = commScore != null ? commScore.doubleValue() : null; }
    public void setCommScore(Number commScore) { this.commScore = commScore != null ? commScore.doubleValue() : null; }

    public String getCommColor() { return commColor; }
    public void setCommColor(String commColor) { this.commColor = commColor; }

    public Double getInitiativeScore() { return initiativeScore; }
    public void setInitiativeScore(Double initiativeScore) { this.initiativeScore = initiativeScore; }
    public void setInitiativeScore(Integer initiativeScore) { this.initiativeScore = initiativeScore != null ? initiativeScore.doubleValue() : null; }
    public void setInitiativeScore(Number initiativeScore) { this.initiativeScore = initiativeScore != null ? initiativeScore.doubleValue() : null; }

    public String getInitiativeColor() { return initiativeColor; }
    public void setInitiativeColor(String initiativeColor) { this.initiativeColor = initiativeColor; }

    public Double getDecisionScore() { return decisionScore; }
    public void setDecisionScore(Double decisionScore) { this.decisionScore = decisionScore; }
    public void setDecisionScore(Integer decisionScore) { this.decisionScore = decisionScore != null ? decisionScore.doubleValue() : null; }
    public void setDecisionScore(Number decisionScore) { this.decisionScore = decisionScore != null ? decisionScore.doubleValue() : null; }

    public String getDecisionColor() { return decisionColor; }
    public void setDecisionColor(String decisionColor) { this.decisionColor = decisionColor; }

    public Double getLeadershipScore() { return leadershipScore; }
    public void setLeadershipScore(Double leadershipScore) { this.leadershipScore = leadershipScore; }
    public void setLeadershipScore(Integer leadershipScore) { this.leadershipScore = leadershipScore != null ? leadershipScore.doubleValue() : null; }
    public void setLeadershipScore(Number leadershipScore) { this.leadershipScore = leadershipScore != null ? leadershipScore.doubleValue() : null; }

    public String getLeadershipColor() { return leadershipColor; }
    public void setLeadershipColor(String leadershipColor) { this.leadershipColor = leadershipColor; }

    public Double getStrategicScore() { return strategicScore; }
    public void setStrategicScore(Double strategicScore) { this.strategicScore = strategicScore; }
    public void setStrategicScore(Integer strategicScore) { this.strategicScore = strategicScore != null ? strategicScore.doubleValue() : null; }
    public void setStrategicScore(Number strategicScore) { this.strategicScore = strategicScore != null ? strategicScore.doubleValue() : null; }

    public String getStrategicColor() { return strategicColor; }
    public void setStrategicColor(String strategicColor) { this.strategicColor = strategicColor; }

    public Double getSkillsScore() { return skillsScore; }
    public void setSkillsScore(Double skillsScore) { this.skillsScore = skillsScore; }
    public void setSkillsScore(Integer skillsScore) { this.skillsScore = skillsScore != null ? skillsScore.doubleValue() : null; }
    public void setSkillsScore(Number skillsScore) { this.skillsScore = skillsScore != null ? skillsScore.doubleValue() : null; }

    public String getSkillsColor() { return skillsColor; }
    public void setSkillsColor(String skillsColor) { this.skillsColor = skillsColor; }

    public Double getAdaptabilityScore() { return adaptabilityScore; }
    public void setAdaptabilityScore(Double adaptabilityScore) { this.adaptabilityScore = adaptabilityScore; }
    public void setAdaptabilityScore(Integer adaptabilityScore) { this.adaptabilityScore = adaptabilityScore != null ? adaptabilityScore.doubleValue() : null; }
    public void setAdaptabilityScore(Number adaptabilityScore) { this.adaptabilityScore = adaptabilityScore != null ? adaptabilityScore.doubleValue() : null; }

    public String getAdaptabilityColor() { return adaptabilityColor; }
    public void setAdaptabilityColor(String adaptabilityColor) { this.adaptabilityColor = adaptabilityColor; }

    public Double getAnalysisScore() { return analysisScore; }
    public void setAnalysisScore(Double analysisScore) { this.analysisScore = analysisScore; }
    public void setAnalysisScore(Integer analysisScore) { this.analysisScore = analysisScore != null ? analysisScore.doubleValue() : null; }
    public void setAnalysisScore(Number analysisScore) { this.analysisScore = analysisScore != null ? analysisScore.doubleValue() : null; }

    public String getAnalysisColor() { return analysisColor; }
    public void setAnalysisColor(String analysisColor) { this.analysisColor = analysisColor; }

    public String getGeneralAbilitiesColor() { return generalAbilitiesColor; }
    public void setGeneralAbilitiesColor(String generalAbilitiesColor) { this.generalAbilitiesColor = generalAbilitiesColor; }

    public Double getAbstractScore() { return abstractScore; }
    public void setAbstractScore(Double abstractScore) { this.abstractScore = abstractScore; }
    public void setAbstractScore(Integer abstractScore) { this.abstractScore = abstractScore != null ? abstractScore.doubleValue() : null; }
    public void setAbstractScore(Number abstractScore) { this.abstractScore = abstractScore != null ? abstractScore.doubleValue() : null; }

    public String getAbstractColor() { return abstractColor; }
    public void setAbstractColor(String abstractColor) { this.abstractColor = abstractColor; }

    public Double getNumericalScore() { return numericalScore; }
    public void setNumericalScore(Double numericalScore) { this.numericalScore = numericalScore; }
    public void setNumericalScore(Integer numericalScore) { this.numericalScore = numericalScore != null ? numericalScore.doubleValue() : null; }
    public void setNumericalScore(Number numericalScore) { this.numericalScore = numericalScore != null ? numericalScore.doubleValue() : null; }

    public String getNumericalColor() { return numericalColor; }
    public void setNumericalColor(String numericalColor) { this.numericalColor = numericalColor; }

    public Double getVerbalScore() { return verbalScore; }
    public void setVerbalScore(Double verbalScore) { this.verbalScore = verbalScore; }
    public void setVerbalScore(Integer verbalScore) { this.verbalScore = verbalScore != null ? verbalScore.doubleValue() : null; }
    public void setVerbalScore(Number verbalScore) { this.verbalScore = verbalScore != null ? verbalScore.doubleValue() : null; }

    public String getVerbalColor() { return verbalColor; }
    public void setVerbalColor(String verbalColor) { this.verbalColor = verbalColor; }

    public Map<Integer, CompetencyDetailDto> getCompetencyPages() { return competencyPages; }
    public void setCompetencyPages(Map<Integer, CompetencyDetailDto> competencyPages) { this.competencyPages = competencyPages; }

    public String getGrowGoalText() { return growGoalText; }
    public void setGrowGoalText(String growGoalText) { this.growGoalText = growGoalText; }

    public String getGrowRealityText() { return growRealityText; }
    public void setGrowRealityText(String growRealityText) { this.growRealityText = growRealityText; }

    public String getGrowOptionsText() { return growOptionsText; }
    public void setGrowOptionsText(String growOptionsText) { this.growOptionsText = growOptionsText; }

    public String getGrowWillText() { return growWillText; }
    public void setGrowWillText(String growWillText) { this.growWillText = growWillText; }

    // =========================================================================
    // Nested DTOs for Structured & Reusable Data Representation
    // =========================================================================

    /**
     * Encapsulates all data required for a Detailed Competency page (Pages 7–14).
     */
    public static class CompetencyDetailDto implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer pageNum;
        private String candidateId;
        private String competencyTitle;
        private String competencyDesc;
        private Double competencyScore;
        private String competencyColor;

        // Row 1
        private String indicator1Color;
        private String req1;
        private String result1;
        private String rec1;

        // Row 2
        private String indicator2Color;
        private String req2;
        private String result2;
        private String rec2;

        // Row 3 (Optional / null on 2-row pages like Page 11)
        private String indicator3Color;
        private String req3;
        private String result3;
        private String rec3;

        public CompetencyDetailDto() {}

        public Integer getPageNum() { return pageNum; }
        public void setPageNum(Integer pageNum) { this.pageNum = pageNum; }

        public String getCandidateId() { return candidateId; }
        public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

        public String getCompetencyTitle() { return competencyTitle; }
        public void setCompetencyTitle(String competencyTitle) { this.competencyTitle = competencyTitle; }

        public String getCompetencyDesc() { return competencyDesc; }
        public void setCompetencyDesc(String competencyDesc) { this.competencyDesc = competencyDesc; }

        public Double getCompetencyScore() { return competencyScore; }
        public void setCompetencyScore(Double competencyScore) { this.competencyScore = competencyScore; }
        public void setCompetencyScore(Integer competencyScore) { this.competencyScore = competencyScore != null ? competencyScore.doubleValue() : null; }
        public void setCompetencyScore(Number competencyScore) { this.competencyScore = competencyScore != null ? competencyScore.doubleValue() : null; }

        public String getCompetencyColor() { return competencyColor; }
        public void setCompetencyColor(String competencyColor) { this.competencyColor = competencyColor; }

        public String getIndicator1Color() { return indicator1Color; }
        public void setIndicator1Color(String indicator1Color) { this.indicator1Color = indicator1Color; }

        public String getReq1() { return req1; }
        public void setReq1(String req1) { this.req1 = req1; }

        public String getResult1() { return result1; }
        public void setResult1(String result1) { this.result1 = result1; }

        public String getRec1() { return rec1; }
        public void setRec1(String rec1) { this.rec1 = rec1; }

        public String getIndicator2Color() { return indicator2Color; }
        public void setIndicator2Color(String indicator2Color) { this.indicator2Color = indicator2Color; }

        public String getReq2() { return req2; }
        public void setReq2(String req2) { this.req2 = req2; }

        public String getResult2() { return result2; }
        public void setResult2(String result2) { this.result2 = result2; }

        public String getRec2() { return rec2; }
        public void setRec2(String rec2) { this.rec2 = rec2; }

        public String getIndicator3Color() { return indicator3Color; }
        public void setIndicator3Color(String indicator3Color) { this.indicator3Color = indicator3Color; }

        public String getReq3() { return req3; }
        public void setReq3(String req3) { this.req3 = req3; }

        public String getResult3() { return result3; }
        public void setResult3(String result3) { this.result3 = result3; }

        public String getRec3() { return rec3; }
        public void setRec3(String rec3) { this.rec3 = rec3; }

        /**
         * Converts this individual competency page data into a map of Thymeleaf model attributes.
         */
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<>();
            if (pageNum != null) map.put("pageNum", pageNum);
            if (candidateId != null) map.put("candidateId", candidateId);
            if (competencyTitle != null) map.put("competencyTitle", competencyTitle);
            if (competencyDesc != null) map.put("competencyDesc", competencyDesc);
            if (competencyScore != null) map.put("competencyScore", competencyScore);
            if (competencyColor != null) map.put("competencyColor", competencyColor);

            if (indicator1Color != null) map.put("indicator1Color", indicator1Color);
            if (req1 != null) map.put("req1", req1);
            if (result1 != null) map.put("result1", result1);
            if (rec1 != null) map.put("rec1", rec1);

            if (indicator2Color != null) map.put("indicator2Color", indicator2Color);
            if (req2 != null) map.put("req2", req2);
            if (result2 != null) map.put("result2", result2);
            if (rec2 != null) map.put("rec2", rec2);

            if (indicator3Color != null) map.put("indicator3Color", indicator3Color);
            if (req3 != null) map.put("req3", req3);
            if (result3 != null) map.put("result3", result3);
            if (rec3 != null) map.put("rec3", rec3);

            return map;
        }
    }

    // =========================================================================
    // Helper & Conversion Methods for Spring MVC Model & Thymeleaf Context
    // =========================================================================

    /**
     * Converts the entire ReportContextDto into a flat Key-Value Map
     * suitable for setting model attributes across all templates.
     */
    public Map<String, Object> toFlatMap() {
        Map<String, Object> map = new HashMap<>();

        // Shared / Global
        map.put("candidateId", candidateId);
        map.put("candidateName", candidateName);
        map.put("reportDate", reportDate);
        map.put("evaluationPurpose", evaluationPurpose);
        map.put("companyLogoBase64", companyLogoBase64);
        map.put("logoUrl", logoUrl);

        // Page 1
        map.put("resultScore", resultScore);

        // Page 2
        map.put("socialScore", socialScore);
        map.put("socialRisk", socialRisk);
        map.put("socialInterpretation", socialInterpretation);
        map.put("centralScore", centralScore);
        map.put("centralRisk", centralRisk);
        map.put("centralInterpretation", centralInterpretation);

        // Page 4
        map.put("reservedScore", reservedScore);
        map.put("reservedText", reservedText);
        map.put("emotionalityScore", emotionalityScore);
        map.put("emotionalityText", emotionalityText);
        map.put("hostilityScore", hostilityScore);
        map.put("hostilityText", hostilityText);
        map.put("impulsivityScore", impulsivityScore);
        map.put("impulsivityText", impulsivityText);
        map.put("rigidityScore", rigidityScore);
        map.put("rigidityText", rigidityText);
        map.put("unconventionalityScore", unconventionalityScore);
        map.put("unconventionalityText", unconventionalityText);

        // Page 5
        map.put("overallScore", overallScore);
        map.put("overallColor", overallColor);
        map.put("commScore", commScore);
        map.put("commColor", commColor);
        map.put("initiativeScore", initiativeScore);
        map.put("initiativeColor", initiativeColor);
        map.put("decisionScore", decisionScore);
        map.put("decisionColor", decisionColor);
        map.put("leadershipScore", leadershipScore);
        map.put("leadershipColor", leadershipColor);
        map.put("strategicScore", strategicScore);
        map.put("strategicColor", strategicColor);
        map.put("skillsScore", skillsScore);
        map.put("skillsColor", skillsColor);
        map.put("adaptabilityScore", adaptabilityScore);
        map.put("adaptabilityColor", adaptabilityColor);
        map.put("analysisScore", analysisScore);
        map.put("analysisColor", analysisColor);

        map.put("generalAbilitiesColor", generalAbilitiesColor);
        map.put("abstractScore", abstractScore);
        map.put("abstractColor", abstractColor);
        map.put("numericalScore", numericalScore);
        map.put("numericalColor", numericalColor);
        map.put("verbalScore", verbalScore);
        map.put("verbalColor", verbalColor);

        // Page 15
        map.put("growGoalText", growGoalText);
        map.put("growRealityText", growRealityText);
        map.put("growOptionsText", growOptionsText);
        map.put("growWillText", growWillText);

        return map;
    }

    /**
     * Populates a Spring MVC {@link Model} with all report variables.
     */
    public void populateModel(Model model) {
        toFlatMap().forEach(model::addAttribute);
    }

    /**
     * Populates a Spring MVC {@link Model} for a specific detailed competency page (Pages 7–14).
     */
    public void populateCompetencyModel(Model model, int pageNum) {
        populateModel(model);
        CompetencyDetailDto pageDto = getCompetencyPage(pageNum);
        if (pageDto != null) {
            pageDto.toMap().forEach(model::addAttribute);
        } else {
            model.addAttribute("pageNum", pageNum);
        }
    }

    /**
     * Creates and populates a Thymeleaf {@link Context} ready for PDF template processing.
     */
    public Context toThymeleafContext() {
        Context context = new Context(new Locale("ar"));
        context.setVariables(toFlatMap());
        return context;
    }

    /**
     * Creates and populates a Thymeleaf {@link Context} for a specific competency page.
     */
    public Context toThymeleafContextForCompetency(int pageNum) {
        Context context = toThymeleafContext();
        CompetencyDetailDto pageDto = getCompetencyPage(pageNum);
        if (pageDto != null) {
            pageDto.toMap().forEach(context::setVariable);
        } else {
            context.setVariable("pageNum", pageNum);
        }
        return context;
    }

    /**
     * Retrieves or creates default competency data for a given page number (7..14).
     */
    public CompetencyDetailDto getCompetencyPage(int pageNum) {
        if (competencyPages != null && competencyPages.containsKey(pageNum)) {
            return competencyPages.get(pageNum);
        }
        return getDefaultCompetencyPage(pageNum, this.candidateId);
    }

    /**
     * Helper factory creating default standardized competency data for each page.
     */
    public static CompetencyDetailDto getDefaultCompetencyPage(int pageNum, String candidateId) {
        String cid = candidateId != null ? candidateId : "PCIV126371";
        CompetencyDetailDto dto = new CompetencyDetailDto();
        dto.setPageNum(pageNum);
        dto.setCandidateId(cid);

        switch (pageNum) {
            case 7: // التواصل والتأثير الفعال
                dto.setCompetencyTitle("التواصل والتأثير الفعال");
                dto.setCompetencyDesc("قدرة القائد على نقل الأفكار والمعلومات بوضوح وإقناع، والتأثير الإيجابي في الآخرين، وبناء علاقات عمل قوية تدعم تحقيق أهداف المنظمة.");
                dto.setCompetencyScore(4);
                dto.setCompetencyColor("#558b6e");
                dto.setIndicator1Color("#558b6e");
                dto.setReq1("ينصت باهتمام ويتفاعل بإيجابية مع الآخرين لبناء علاقات مهنية قوية.");
                dto.setResult1("يتفاعل بشكل استثنائي مع الآخرين، مما يعزز العلاقات القيادية بفعالية. يُظهر قدرة متميزة على بناء الثقة وتسهيل التعاون بين الوحدات المختلفة.");
                dto.setRec1("حافظ على الاستمرار في بناء شبكات علاقات استراتيجية. وفّر فرصاً لقيادة مبادرات تعاونية معقدة بين الأجهزة المختلفة.");
                dto.setIndicator2Color("#d9776c");
                dto.setReq2("يعبّر عن الأفكار والمشاعر بوضوح وثقة للتأثير في الآخرين.");
                dto.setResult2("يميل إلى التحفظ في التعبير عن الأفكار في التجمعات الكبيرة، مما يحدّ أحياناً من قدرته على التأثير المباشر وإقناع الفرق بالقرارات.");
                dto.setRec2("شجّع المشاركة الفعالة في الاجتماعات القيادية والتحدث أمام الفرق. وفّر تدريباً عملياً على مهارات العرض والإقناع المتقدم.");
                dto.setIndicator3Color("#d98a44");
                dto.setReq3("يتحاور بمرونة وتفهم، مقدراً وجهات النظر المختلفة للوصول إلى حلول توافقية.");
                dto.setResult3("يوازن بين الدفاع عن موقفه والاستماع للآراء المعارضة، مما يتيح التوصل إلى تفاهمات عملية في أغلب المواقف.");
                dto.setRec3("وفّر فرصاً لقيادة مفاوضات وإدارة حوارات استراتيجية لتعزيز المرونة وسرعة الوصول إلى حلول مبتكرة.");
                break;

            case 8: // المبادرة
                dto.setCompetencyTitle("المبادرة");
                dto.setCompetencyDesc("قدرة القائد على التحرك من تلقاء نفسه دون انتظار الأوامر، عبر اقتراح الأفكار واتخاذ الإجراءات المناسبة في الوقت المناسب لتحسين العمل وحل المشكلات وتحقيق الأهداف بسرعة وفعالية.");
                dto.setCompetencyScore(2);
                dto.setCompetencyColor("#d98a44");
                dto.setIndicator1Color("#d98a44");
                dto.setReq1("يتخذ الإجراءات بحسم لتحقيق الأهداف دون انتظار التوجيهات.");
                dto.setResult1("يتصرف بشكل معتدل دون انتظار الأوامر، مما يدعم المبادرة بفعالية في معظم الحالات. يظهر توازناً بين الاستقلالية والالتزام بالتوجيهات العسكرية.");
                dto.setRec1("وفر فرصاً لقيادة مبادرات عسكرية بشكل مستقل لتعزيز الاتساق والمرونة. رشّح مراقبة أنواع مختلفة من المواقف الحرجة لتحسين الاستجابة. شجع طلب الملاحظات من القادة والزملاء لتطوير مهارات المبادرة.");
                dto.setIndicator2Color("#558b6e");
                dto.setReq2("يبادر بشكل استباقي لتطوير الحلول وتنفيذ المبادرات الاستراتيجية بكفاءة.");
                dto.setResult2("يطور مبادرات استراتيجية بثقة، معززاً المبادرة الفعالة في القيادة العسكرية. يُظهر استباقية في تحديد الاحتياجات وتنفيذ الحلول قبل تفاقم المشكلات.");
                dto.setRec2("تحدّ الفرد لقيادة مبادرات استراتيجية رئيسية أو إرشاد الزملاء حول التخطيط الاستباقي. وفر تدريباً متقدماً في القيادة الاستراتيجية والابتكار لتعزيز التأثير. شجع الموازنة بين المبادرات القوية والتركيز على المهام في المواقف المعقدة.");
                dto.setIndicator3Color("#d98a44");
                dto.setReq3("يُظهر شجاعة في المبادرة لاتخاذ خطوات استباقية وحل المشكلات بفعالية.");
                dto.setResult3("يوازن بين الحذر والإقدام عند اتخاذ المبادرات، مما يدعم أداءً متسقاً في المهام القيادية. يبدو مستعداً لاتخاذ خطوات محسوبة عند الحاجة.");
                dto.setRec3("وفر فرصاً لقيادة مبادرات مستقلة لتعزيز الاتساق والقدرة على التكيف. اقترح مراقبة أنماط متنوعة من المواقف الحرجة لتحسين الاستجابة. شجع طلب الملاحظات من القادة والزملاء لتطوير مهارات المبادرة.");
                break;

            case 9: // اتخاذ القرار وتحمل المسؤولية
                dto.setCompetencyTitle("اتخاذ القرار وتحمل المسؤولية");
                dto.setCompetencyDesc("قدرة القائد على اختيار القرار الصحيح في الوقت المناسب اعتمادًا على المعلومات المتاحة، مع تحمل نتائج قراراته بكل التزام وشجاعة.");
                dto.setCompetencyScore(3);
                dto.setCompetencyColor("#d98a44");
                dto.setIndicator1Color("#d98a44");
                dto.setReq1("يُظهر ثقة كبيرة في اتخاذ القرارات وتحمل النتائج بشجاعة.");
                dto.setResult1("يتخذ القرارات بثقة معتدلة تدعم الأداء المتوازن في مختلف المواقف العملياتية. يبدو قادراً على تحمل مسؤولية القرارات الروتينية والمتوسطة التعقيد.");
                dto.setRec1("وفّر فرصاً لإدارة مواقف قرار أكثر تعقيداً بشكل مستقل لتعزيز الاتساق والمرونة. رشّح مراقبة سيناريوهات عملياتية متنوعة لتحسين الاستجابة. شجع طلب ملاحظات من القادة والزملاء لتطوير مهارات اتخاذ القرار.");
                dto.setIndicator2Color("#d98a44");
                dto.setReq2("يمتلك قدرة معرفية متقدمة لتحليل المعلومات واتخاذ قرارات مسؤولة.");
                dto.setResult2("يحلل المعلومات بكفاءة معقولة تدعم اتخاذ قرارات متوازنة في معظم المواقف العملياتية. يُظهر التزاماً مقبولاً بتحمل نتائج قراراته في السياقات المألوفة.");
                dto.setRec2("وفّر فرصاً لقيادة عمليات معقدة بشكل مستقل لتعزيز اتساق القرارات والقدرة على التكيف. رشّح مراقبة مواقف تكتيكية متنوعة لتحسين الاستجابة. شجع طلب التغذية الراجعة من الرؤساء والزملاء لتطوير مهارات اتخاذ القرارات الاستراتيجية.");
                dto.setIndicator3Color("#d98a44");
                dto.setReq3("يتسم بالدقة والانضباط عند اتخاذ القرارات وتحمل المسؤولية بشجاعة.");
                dto.setResult3("يتعامل مع القرارات بدقة متوازنة تدعم تحمله للمسؤولية بشكل مناسب. يبدو منضبطاً ومستجيباً لمتطلبات المواقف العملياتية.");
                dto.setRec3("وفّر فرصاً لإدارة قرارات استراتيجية بشكل مستقل لتعزيز الاتساق والمرونة. رشّح مراقبة مواقف عملياتية متنوعة لتحسين الاستجابة. شجع طلب ملاحظات من الرؤساء والأقران لصقل مهارات اتخاذ القرار وتحمل المسؤولية.");
                break;

            case 10: // القيادة الملهمة
                dto.setCompetencyTitle("القيادة الملهمة");
                dto.setCompetencyDesc("قدرة القائد على تحفيز الآخرين بروح إيجابية ورؤية واضحة، وجعلهم يؤمنون بالأهداف ويعملون بحماس لتحقيقها من خلال القدوة والتأثير الإيجابي.");
                dto.setCompetencyScore(3);
                dto.setCompetencyColor("#d98a44");
                dto.setIndicator1Color("#d98a44");
                dto.setReq1("يبدي حماساً كبيراً لتحفيز الآخرين وإلهامهم نحو تحقيق الأهداف العسكرية.");
                dto.setResult1("يتفاعل مع الأفراد بحماس متوازن يدعم القيادة الملهمة بفعالية. يظهر وضوحاً واستجابة في توجيه الوحدات نحو الأهداف الاستراتيجية.");
                dto.setRec1("وفّر فرصاً لقيادة مبادرات تحفيزية بشكل مستقل لتعزيز الاتساق والمرونة. رشّح مراقبة أنواع مختلفة من الأفراد والمواقف لتحسين الاستجابة. شجع طلب ملاحظات من الأفراد والزملاء لتطوير مهارات القيادة الملهمة.");
                dto.setIndicator2Color("#d98a44");
                dto.setReq2("يعكس تفاؤلاً يشجع الآخرين ويعزز التزامهم بالأهداف العسكرية.");
                dto.setResult2("يحافظ على نظرة متوازنة تجمع بين الواقعية والإيجابية، مما يدعم القيادة الملهمة بفعالية. يظهر ثقة وتفاؤلاً معتدلاً في معظم المواقف العملياتية.");
                dto.setRec2("وفّر فرصاً لقيادة مبادرات تحفيزية بشكل مستقل لتعزيز الاتساق والقدرة على التكيف. اقترح مراقبة أنماط مختلفة من القيادة الملهمة لتحسين الاستجابة. شجع طلب تغذية راجعة من الزملاء والمرؤوسين لتطوير المهارات التحفيزية.");
                dto.setIndicator3Color("#d98a44");
                dto.setReq3("يمتلك قدرة على التأثير في الآخرين لتحفيزهم نحو تحقيق الأهداف المشتركة.");
                dto.setResult3("يؤثر في الآخرين بفعالية متوازنة تدعم القيادة الملهمة بشكل مناسب. يظهر قدرة على توجيه الوحدات وتحفيزها في معظم المواقف العملياتية.");
                dto.setRec3("وفّر فرصاً لقيادة مبادرات تتطلب تأثيراً أكبر لتعزيز الاتساق والقدرة على التكيف. شجع مراقبة أنماط قيادية متنوعة لتحسين الاستجابة. حفز طلب الملاحظات من الجنود والزملاء لتحسين مهارات التأثير والإلهام.");
                break;

            case 11: // التفكير الاستراتيجي (2 rows only)
                dto.setCompetencyTitle("التفكير الاستراتيجي");
                dto.setCompetencyDesc("القدرة على تحليل المعطيات، واستشراف المستقبل، واتخاذ قرارات استراتيجية فعالة.");
                dto.setCompetencyScore(2);
                dto.setCompetencyColor("#d9776c");
                dto.setIndicator1Color("#d9776c");
                dto.setReq1("يبدي اهتماماً استقصائياً لتحليل البيانات وتوقع المستقبل في اتخاذ القرارات الاستراتيجية.");
                dto.setResult1("يبدي اهتماماً محدوداً بالاستقصاء والتعمق في المعلومات، مما يقيّد قدرته على إجراء تحليل استراتيجي شامل. غالباً ما يعتمد على بيانات سطحية دون البحث في البدائل أو السيناريوهات المستقبلية.");
                dto.setRec1("شجّع المشاركة في مشاريع التخطيط الاستراتيجي تحت إشراف قادة ذوي خبرة لتعزيز مهارات الاستقصاء لديهم. قدّم التوجيه حول تقنيات التحليل الاستراتيجي وأدوات استشراف المستقبل. رشّح دورات قصيرة أو قراءات متخصصة في التفكير النقدي وتحليل السيناريوهات. حدد أهدافاً واضحة ومدعومة لتوسيع نطاق الاستقصاء في القرارات العملياتية.");
                dto.setIndicator2Color("#d98a44");
                dto.setReq2("يمتلك قدرة على التفكير المجرد لفهم الأنماط واتخاذ قرارات استراتيجية ناجحة.");
                dto.setResult2("يتعامل بفعالية مع الأنماط ذات التعقيد المتوسط، مما يدعم تحليل المعطيات واتخاذ قرارات استراتيجية متوازنة. يُظهر قدرة مناسبة على التكيف مع التحديات العملياتية الجديدة.");
                dto.setRec2("وفّر فرصاً لقيادة مبادرات التخطيط الاستراتيجي بشكل مستقل لتعزيز الاتساق والمرونة. رشّح مراقبة تحديات عملياتية متنوعة لتحسين الاستجابة. شجّع طلب الملاحظات من القادة الأعلى والأقران لتحسين مهارات التحليل الاستراتيجي.");
                dto.setIndicator3Color(null);
                dto.setReq3(null);
                dto.setResult3(null);
                dto.setRec3(null);
                break;

            case 12: // تطوير المهارات
                dto.setCompetencyTitle("تطوير المهارات");
                dto.setCompetencyDesc("قدرة القائد على تعلم مهارات جديدة باستمرار وتطوير قدراته وقدرات فريقه لتحسين الأداء ومواكبة التغيرات في بيئة العمل.");
                dto.setCompetencyScore(2);
                dto.setCompetencyColor("#d9776c");
                dto.setIndicator1Color("#d98a44");
                dto.setReq1("يطبق مبادئ التحسين المستمر لتطوير المهارات وتعزيز قدرات الفريق العسكري.");
                dto.setResult1("يحدد أهداف أداء واضحة ويتابع التقدم بانتظام، مما يدعم تطوير المهارات بشكل متوازن. يمتلك قدرة معقولة على تعديل المبادرات التدريبية حسب الحاجة.");
                dto.setRec1("وفر فرصاً لقيادة مبادرات تطوير مستقلة لتعزيز الاتساق والمرونة. رشّح مراقبة أنواع مختلفة من برامج التطوير لتحسين الاستجابة. شجّع طلب ملاحظات من الفريق والزملاء لتحسين مهارات إدارة التطوير.");
                dto.setIndicator2Color("#d9776c");
                dto.setReq2("يبدي فضولاً دائماً لتطوير المهارات والقدرات بهدف رفع الأداء العسكري.");
                dto.setResult2("يبدي اهتماماً محدوداً باستكشاف مهارات جديدة، مما يعيق تطوير المهارات القيادية والعسكرية. قد يكتفي بالأساليب التقليدية دون السعي للتحديث أو التطوير.");
                dto.setRec2("شجّع المشاركة في دورات تدريبية عسكرية متقدمة لتعزيز الثقة في التعلم المستمر. قدّم الإرشاد من قادة ذوي خبرة في تطوير المهارات والابتكار. اقترح قراءات أو برامج قصيرة حول القيادة التكيفية والتطوير المهني. ساعد في تحديد أهداف واضحة ومدعومة لاستكشاف أساليب تدريبية جديدة.");
                dto.setIndicator3Color("#d98a44");
                dto.setReq3("يتمتع بدافع قوي لتطوير المهارات والارتقاء بالقدرات القيادية والعسكرية بشكل مستمر.");
                dto.setResult3("يسعى بشكل متوازن لتطوير قدراته، مما يدعم تحسيناً مستمراً في تطوير المهارات. يظهر استعداداً معقولاً لتعلم مهارات جديدة وتطبيقها.");
                dto.setRec3("وفر فرصاً لقيادة مبادرات تطوير مستقلة لتعزيز الاتساق والمرونة. اقترح مراقبة أساليب تطوير متنوعة لتحسين الاستجابة. شجع طلب ملاحظات من الزملاء والمرؤوسين لتحسين مهارات إدارة التطوير.");
                break;

            case 13: // القدرة على التكيف
                dto.setCompetencyTitle("القدرة على التكيف");
                dto.setCompetencyDesc("القدرة على التكيف مع التغيرات المفاجئة وتعديل الخطط بمرونة لضمان استمرارية العمل وتحقيق الأهداف بكفاءة.");
                dto.setCompetencyScore(3);
                dto.setCompetencyColor("#d98a44");
                dto.setIndicator1Color("#d98a44");
                dto.setReq1("يتسم بمرونة عالية في التكيف مع التغيرات المفاجئة لضمان استمرارية العمليات العسكرية.");
                dto.setResult1("يتعامل مع الضغوط بشكل متزن، مما يمكنه من التكيف مع التغيرات التكتيكية بفعالية معقولة. يُظهر قدرة على استعادة التوازن بعد النكسات والحفاظ على جاهزية الوحدة.");
                dto.setRec1("وفّر فرصاً لقيادة عمليات في بيئات ديناميكية لتعزيز المرونة والاستجابة. شجّع مراقبة أساليب قيادية متنوعة في إدارة الأزمات. سهّل الحصول على تغذية راجعة من الرؤساء والزملاء لتحسين مهارات التكيف.");
                dto.setIndicator2Color("#d98a44");
                dto.setReq2("يتمتع بقدرة كبيرة على التكيف مع التغيرات لضمان استمرارية العمليات العسكرية.");
                dto.setResult2("يتعامل مع التغيرات بتوازن معقول، مما يدعم استمرارية العمليات العسكرية بفعالية. يظهر قدرة مناسبة على تعديل الخطط عند الحاجة.");
                dto.setRec2("وفّر فرصاً لقيادة عمليات معقدة بشكل مستقل لتعزيز الاتساق والقدرة على التكيف. رشّح مراقبة أنواع مختلفة من السيناريوهات العسكرية لتحسين الاستجابة. شجّع طلب الملاحظات من القادة الأعلى والأقران لتطوير مهارات إدارة التغيير.");
                dto.setIndicator3Color("#d98a44");
                dto.setReq3("يظهر قدرة قوية على تحمل الضغوط لتعديل الخطط بمرونة في البيئات العملياتية المتقلبة.");
                dto.setResult3("يتعامل مع الضغوط بشكل متزن، مما يدعم قدرته على التكيف مع التغيرات العملياتية بفعالية. يظهر استقراراً معقولاً عند مواجهة المواقف غير المتوقعة في الميدان.");
                dto.setRec3("وفّر فرصاً لقيادة عمليات معقدة بشكل مستقل لتعزيز الثبات والقدرة على التكيف. اقترح مراقبة أنواع متنوعة من المواقف الضاغطة لتحسين الاستجابة. شجّع طلب التغذية الراجعة من الرؤساء والزملاء لتطوير مهارات إدارة الضغوط.");
                break;

            case 14: // التحليل والتخطيط المنهجي
                dto.setCompetencyTitle("التحليل والتخطيط المنهجي");
                dto.setCompetencyDesc("قدرة القائد على دراسة المواقف بعناية، وفهم المعلومات بشكل منطقي، ثم وضع خطط منظمة ومدروسة تساعد على تحقيق الأهداف بكفاءة وفعالية.");
                dto.setCompetencyScore(4);
                dto.setCompetencyColor("#558b6e");
                dto.setIndicator1Color("#d98a44");
                dto.setReq1("يميل إلى النظام والترتيب عند تحليل المواقف ووضع الخطط بشكل منهجي.");
                dto.setResult1("يتعامل مع المواقف بدرجة متوازنة من التنظيم، مما يدعم التحليل والتخطيط المنهجي بشكل جيد. يظهر منهجية وتنظيماً معقولين في معالجة المعلومات.");
                dto.setRec1("وفر فرصاً لإدارة مشاريع تخطيط عسكري معقدة بشكل مستقل لتعزيز الاتساق والمرونة. اقترح مراقبة أنواع مختلفة من المواقف العملياتية لتحسين الاستجابة. شجع طلب ملاحظات من القادة والزملاء لتطوير مهارات التحليل والتخطيط.");
                dto.setIndicator2Color("#d98a44");
                dto.setReq2("يتمتع بقدرة قوية على التحليل الكمي لدعم التخطيط المنهجي بكفاءة.");
                dto.setResult2("يتعامل بكفاءة معقولة مع البيانات الرقمية والتحليل الكمي، مما يدعم التخطيط المنهجي المتوازن. يستطيع التعرف على الأنماط الأساسية وتطبيق المنطق الرياضي في المواقف العملياتية المعتادة.");
                dto.setRec2("وفّر فرصاً لقيادة مشاريع تخطيط تتطلب تحليلاً كمياً مستقلاً لتعزيز الاتساق. شجّع التعرض لأنواع مختلفة من البيانات العملياتية لتحسين القدرة على التكيف. رتّب جلسات تغذية راجعة منتظمة من الزملاء والقادة الأعلى لتطوير المهارات التحليلية.");
                dto.setIndicator3Color("#558b6e");
                dto.setReq3("يركز بشكل كبير على تحليل المواقف وتصميم خطط منهجية لتحقيق الأهداف العسكرية.");
                dto.setResult3("يتميز بتركيز استثنائي في تحليل المواقف المعقدة وصياغة خطط عسكرية شاملة ومحكمة. يحافظ على انتباه مستمر حتى في أصعب البيئات العملياتية.");
                dto.setRec3("ادعُ القائد لتقديم ورش عمل أو تدريب الآخرين على التحليل والتخطيط المنهجي. وفّر مهام متقدمة تتطلب الموازنة بين التحليل الشامل والاستجابة السريعة. شجّع التطوير المهني المتقدم في القيادة الاستراتيجية وإدارة العمليات المعقدة.");
                break;

            default:
                dto.setCompetencyTitle("الكفاءة السلوكية");
                dto.setCompetencyDesc("وصف الكفاءة السلوكية القيادية.");
                dto.setCompetencyScore(3);
                dto.setCompetencyColor("#d98a44");
                break;
        }

        return dto;
    }

    /**
     * Initializes default data for all 15 pages in the report.
     */
    public static ReportContextDto createDefaultReport(String candidateId) {
        ReportContextDto report = new ReportContextDto();
        if (candidateId != null) {
            report.setCandidateId(candidateId);
        }

        for (int p = 7; p <= 14; p++) {
            report.getCompetencyPages().put(p, getDefaultCompetencyPage(p, report.getCandidateId()));
        }

        return report;
    }
}
