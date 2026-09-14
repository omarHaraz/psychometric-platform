package com.psychometric.platform.features.report.service;

import com.psychometric.platform.features.itembank.gcat.entity.GcatSubtestCode;
import com.psychometric.platform.features.assessment.domain.enums.ReadinessBand;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import com.psychometric.platform.features.assessment.domain.model.GcatSubtestScore;
import com.psychometric.platform.features.assessment.domain.model.TraitScore;
import com.psychometric.platform.features.report.dto.EmploymentReportDto;
import com.psychometric.platform.features.report.dto.EmploymentReportDto.ActionPlanItem;
import com.psychometric.platform.features.report.dto.EmploymentReportDto.DiagnosticPoint;
import com.psychometric.platform.features.report.dto.EmploymentReportDto.EmploymentCompetencyScore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class EmploymentReportGeneratorService {

    private static final Logger log = LoggerFactory.getLogger(EmploymentReportGeneratorService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public EmploymentReportDto generateReport(AssessmentScore score) {
        log.info("Generating Employment Readiness Report for attempt ID: {}", score.getAttempt() != null ? score.getAttempt().getId() : "null");

        AssessmentAttempt attempt = score.getAttempt();
        String candidateName = (attempt != null && attempt.getCandidate() != null) ? attempt.getCandidate().getName() : "مرشح التوظيف";
        String candidateId = (attempt != null && attempt.getCandidate() != null) ? String.valueOf(attempt.getCandidate().getId()) : "N/A";
        String attemptToken = (attempt != null && attempt.getAttemptToken() != null) ? attempt.getAttemptToken() : "EMP-TOKEN";
        String refNumber = "REF-EMP-" + (attemptToken.length() >= 8 ? attemptToken.substring(0, 8).toUpperCase() : attemptToken.toUpperCase());

        String reportDate = (score.getScoredAt() != null)
                ? DATE_FORMATTER.format(score.getScoredAt().atZone(ZoneId.systemDefault()))
                : DATE_FORMATTER.format(java.time.LocalDate.now());

        double pqScore = score.getPersonalityScorePct() != null ? score.getPersonalityScorePct() : 0.0;
        double sjtScore = score.getSjtScorePct() != null ? score.getSjtScorePct() : 0.0;
        double gcatScore = score.getCognitiveScorePct() != null ? score.getCognitiveScorePct() : 0.0;
        double compositeScore = score.getCompositeScore() != null ? score.getCompositeScore() : 0.0;

        // GCAT subtests
        double verbalScore = 0.0;
        double numericalScore = 0.0;
        double abstractScore = 0.0;

        if (score.getGcatSubtestScores() != null) {
            for (GcatSubtestScore gss : score.getGcatSubtestScores()) {
                if (gss.getSubtest() == GcatSubtestCode.VERBAL) verbalScore = gss.getScorePct();
                if (gss.getSubtest() == GcatSubtestCode.NUMERICAL) numericalScore = gss.getScorePct();
                if (gss.getSubtest() == GcatSubtestCode.ABSTRACT) abstractScore = gss.getScorePct();
            }
        }

        // Determine Statuses (متقدم >= 75%, متوسط 60-74.99%, يحتاج تطوير < 60%)
        String pqStatus = evaluateStatus(pqScore);
        String sjtStatus = evaluateStatus(sjtScore);
        String gcatStatus = evaluateStatus(gcatScore);
        String verbalStatus = evaluateStatus(verbalScore);
        String numericalStatus = evaluateStatus(numericalScore);
        String abstractStatus = evaluateStatus(abstractScore);

        // Map components for comparative analysis
        Map<String, Double> components = new LinkedHashMap<>();
        components.put("السمات الشخصية وسلوكيات العمل", pqScore);
        components.put("المواقف المهنية والجدارات (SJT)", sjtScore);
        components.put("القدرات الإدراكية المعرفية (GCAT)", gcatScore);

        String highestComp = Collections.max(components.entrySet(), Map.Entry.comparingByValue()).getKey();
        String lowestComp = Collections.min(components.entrySet(), Map.Entry.comparingByValue()).getKey();

        List<String> deficientComps = new ArrayList<>();
        if (pqScore < 60.0) deficientComps.add("السمات الشخصية وسلوكيات العمل (" + pqScore + "%)");
        if (sjtScore < 60.0) deficientComps.add("المواقف المهنية والجدارات (" + sjtScore + "%)");
        if (gcatScore < 60.0) deficientComps.add("القدرات الإدراكية المعرفية (" + gcatScore + "%)");

        // Determine Case Number (1, 2, 3, 4)
        int sub50Count = 0;
        if (pqScore < 50.0) sub50Count++;
        if (sjtScore < 50.0) sub50Count++;
        if (gcatScore < 50.0) sub50Count++;

        int caseNumber;
        ReadinessBand band;
        String bandLabelAr;
        String bandLabelEn;
        String diagnosticCaseTitle;
        String diagnosticCaseSubtitle;

        if (compositeScore >= 85.0 && sub50Count == 0) {
            caseNumber = 1;
            band = ReadinessBand.EXCELLENT;
            bandLabelAr = "ممتاز";
            bandLabelEn = "Excellent";
            diagnosticCaseTitle = "تكامل الأداء والجدارة الاستثنائية";
            diagnosticCaseSubtitle = "تفوق متوازن واستثنائي في السمات الشخصية، تقدير المواقف والقدرات الإدراكية";
        } else if (compositeScore >= 75.0 && sub50Count == 0) {
            caseNumber = 2;
            band = ReadinessBand.STRONG;
            bandLabelAr = "جيد جداً";
            bandLabelEn = "Very Good";
            diagnosticCaseTitle = "جاهزية مهنية متقدمة مع تفاوت طفيف";
            diagnosticCaseSubtitle = "أداء متقدم ونقاط قوة ملموسة مع فرصة واعدة للارتقاء بأحد الجوانب";
        } else if (compositeScore >= 60.0 && sub50Count < 2) {
            caseNumber = 3;
            band = ReadinessBand.ACCEPTABLE;
            bandLabelAr = "جيد";
            bandLabelEn = "Good";
            diagnosticCaseTitle = "جاهزية أساسية مقيدة بفجوة في أحد الأجزاء";
            diagnosticCaseSubtitle = "امتلاك الحد الأدنى للجاهزية مع وجود فجوة تتطلب تدخلاً تدريبياً قبل التسكين الكامل";
        } else {
            caseNumber = 4;
            band = ReadinessBand.FOUNDATIONAL;
            bandLabelAr = "ضعيف";
            bandLabelEn = "Needs Improvement";
            diagnosticCaseTitle = "حاجة ماسة لإعادة التأهيل المعرفي والسلوكي";
            diagnosticCaseSubtitle = "وجود فجوات حرجة متزامنة تتطلب برنامج إعادة تأهيل مهني شامل";
        }

        // Build Diagnostic Points & Action Plan Items
        List<DiagnosticPoint> diagnosticPoints = new ArrayList<>();
        String actionPlanTitle;
        List<ActionPlanItem> actionPlanItems = new ArrayList<>();

        if (caseNumber == 1) {
            diagnosticPoints.add(new DiagnosticPoint("في السمات الشخصية", "عكست استجاباتك نضجاً انفعالياً عالياً، ومستوى متقدماً من الانضباط الذاتي، والنزاهة، والمبادرة الإيجابية."));
            diagnosticPoints.add(new DiagnosticPoint("في المواقف المهنية (SJT)", "أظهرت اختياراتك فهماً عميقاً لبيئة العمل، وإدراكاً ممتازاً لكيفية التعامل مع المتعاملين وحل النزاعات باحترافية."));
            diagnosticPoints.add(new DiagnosticPoint("في القدرات المعرفية الثلاثة", "حققت درجات متقدمة في التحليل اللفظي والمنطق العددي والاستنتاجي، مما يعكس سرعة البديهة والقدرة على معالجة البيانات المعقدة."));

            actionPlanTitle = "خطة الاستثمار المهني والانطلاق (Direct Investment Path)";
            actionPlanItems.add(new ActionPlanItem("01", "الترشيح المباشر للمسارات الوظيفية الحيوية",
                    "الترشيح المباشر للمسارات الوظيفية التي تتطلب مهارات قيادية وتنسيقية ومسؤوليات مبكرة، وإسناد مشاريع استراتيجية استباقية.", "المسار المهني"));
            actionPlanItems.add(new ActionPlanItem("02", "التوجيه نحو الابتكار المؤسسي وإدارة المشاريع",
                    "توجيه القدرات نحو الابتكار المؤسسي، وإدارة المشاريع الرشيقة، والتدريب المتقدم على أدوات الأتمتة والذكاء الاصطناعي.", "تطوير مهارات التميز"));

        } else if (caseNumber == 2) {
            diagnosticPoints.add(new DiagnosticPoint("تحليل التميز والأداء",
                    "أظهرت جاهزية متقدمة ونقاط قوة ملموسة؛ حيث برز تميزك الأعلى في [" + highestComp + "]، بينما أظهرت استقراراً جيداً في الجزأين الآخرين دون أي تدنٍ حرج."));
            diagnosticPoints.add(new DiagnosticPoint("تشخيص التفاوت",
                    "وجود هامش بسيط للارتقاء في [" + lowestComp + "] للوصول به إلى مستوى التميز الكامل وسد أي تفاوت طفيف."));

            actionPlanTitle = "خطة التمكين والارتقاء الموجهة (Targeted Empowerment Plan)";
            if (lowestComp.contains("السمات")) {
                actionPlanItems.add(new ActionPlanItem("01", "تعزيز مهارات التكيف وإدارة الأولويات",
                        "تعزيز مهارات التكيف مع بيئات العمل المتغيرة وإدارة الأولويات تحت ضغط الوقت من خلال جلسات كوتشينغ مهني موجهة.", "تطوير السمات"));
            } else if (lowestComp.contains("المواقف")) {
                actionPlanItems.add(new ActionPlanItem("01", "مراجعة استراتيجيات التعامل مع المتعاملين والتواصل",
                        "مراجعة استراتيجيات التعامل مع المتعاملين المتطلبين ودبلوماسية التواصل مع فرق العمل عبر محاكاة مواقف العمل اليومية.", "المواقف والجدارات"));
            } else {
                actionPlanItems.add(new ActionPlanItem("01", "صقل مهارات قراءة وتحليل البيانات السريعة",
                        "صقل مهارات قراءة وتحليل البيانات والجداول الإحصائية السريعة عبر تطبيقات عملية وتمارين تحليلية مركزة.", "القدرات المعرفية"));
            }
            actionPlanItems.add(new ActionPlanItem("02", "التوجيه المهني والتكامل في فرق العمل",
                    "الاندماج في فرق عمل متعددة التخصصات والاستفادة من برنامج التوجيه المهني (Mentorship) لترسيخ أفضل الممارسات.", "التمكين المهني"));

        } else if (caseNumber == 3) {
            String weakName = deficientComps.isEmpty() ? lowestComp : String.join("، ", deficientComps);
            diagnosticPoints.add(new DiagnosticPoint("التشخيص العام",
                    "تمتلك قاعدة صالحة للانطلاق في سوق العمل، إلا أن مؤشرات الاختبار رصدت فجوة واضحة تتركز في [" + weakName + "]، وهي الفجوة التي خفضت المعدل العام وتستوجب تدخلاً تدريبياً قبل التسكين الكامل."));

            actionPlanTitle = "خطة التطوير العلاجية المرتبطة بالجزء الضعيف (Remedial Action Plan)";
            if (pqScore < 60.0 || lowestComp.contains("السمات")) {
                actionPlanItems.add(new ActionPlanItem("01", "ورشة إدارة الذات وبناء المرونة النفسية",
                        "الالتحاق بورشة تدريبية مكثفة في 'إدارة الذات، وبناء المرونة النفسية، والانضباط المؤسسي'.", "السمات الشخصية"));
                actionPlanItems.add(new ActionPlanItem("02", "تدوين وتتبع المهام ذاتياً",
                        "التدرب على تدوين وتتبع المهام ذاتياً والالتزام بجداول زمنية صارمة لرفع كفاءة الإنتاجية اليومية.", "التطبيق الميداني"));
            }
            if (sjtScore < 60.0 || lowestComp.contains("المواقف")) {
                actionPlanItems.add(new ActionPlanItem("03", "دورة السلوك الوظيفي وأخلاقيات المهنة",
                        "الانخراط في دورة 'السلوك الوظيفي، وأخلاقيات المهنة، وحل مشكلات بيئة العمل وخدمة المتعاملين'.", "المواقف المهنية SJT"));
                actionPlanItems.add(new ActionPlanItem("04", "محاكاة السيناريوهات التفاعلية",
                        "التدرب عبر دراسات حالة وسيناريوهات تفاعلية لترتيب الخيارات المهنية الصحيحة واتخاذ القرارات المتوازنة.", "التطبيق الميداني"));
            }
            if (gcatScore < 60.0 || lowestComp.contains("القدرات")) {
                String weakSub = (verbalScore < 60 ? "الاستدلال اللفظي " : "") +
                                (numericalScore < 60 ? "الاستدلال العددي " : "") +
                                (abstractScore < 60 ? "الاستدلال المنطقي " : "");
                if (weakSub.isBlank()) weakSub = "القدرات المعرفية الإدراكية";
                actionPlanItems.add(new ActionPlanItem("05", "تدريب مكثف على التطبيقات الرياضية وفهم النصوص",
                        "تدريب مكثف على [" + weakSub.trim() + "] عبر التطبيقات الرياضية العملية، واستخدام برنامج Excel، وفهم النصوص المهنية.", "القدرات المعرفية"));
                actionPlanItems.add(new ActionPlanItem("06", "تمارين استدلالية يومية",
                        "حل تمارين استدلالية يومية لمدة (15-20 دقيقة) لتنشيط المهارات التحليلية وسرعة المعالجة الذهنية.", "التطبيق الميداني"));
            }

        } else {
            String weakList = deficientComps.isEmpty() ? (lowestComp + " (" + Collections.min(components.values()) + "%)") : String.join(" و ", deficientComps);
            diagnosticPoints.add(new DiagnosticPoint("التشخيص العام",
                    "تشير نتائجك إلى عدم اكتمال مقومات الجاهزية الوظيفية في الوقت الحالي، نظراً لوجود صعوبات وفجوات متزامنة في [" + weakList + "]؛ مما يجعل الانتقال المباشر لبيئة العمل محفوفاً بالتحديات التشغيلية والسلوكية دون تأهيل مكثف."));

            actionPlanTitle = "خطة إعادة التأهيل الشاملة (Comprehensive Re-skilling Path)";
            actionPlanItems.add(new ActionPlanItem("01", "برنامج ثقافة العمل والمسؤولية المهنية",
                    "برنامج تأسيسي مكثف في 'ثقافة العمل، والنزاهة، والمسؤولية الشخصية والمهنية، وإدارة الانفعالات'.", "مسار تصحيح السمات"));
            actionPlanItems.add(new ActionPlanItem("02", "تدريب محاكاة ميداني وتمثيل أدوار (Role-Playing)",
                    "تدريب محاكاة ميداني وتمثيل أدوار لتعلم الاستجابات الصحيحة في بيئة العمل والتعامل المهني مع المشرفين والجمهور.", "مسار التدريب على المواقف"));
            actionPlanItems.add(new ActionPlanItem("03", "برنامج تقوية في الحساب والاستيعاب والمنطق",
                    "برنامج تقوية في العمليات الحسابية الأساسية، الفهم اللغوي واستيعاب المقروء، والمنطق الاستدلالي قبل إعادة التقييم.", "مسار تقوية الأساسيات الإدراكية"));
        }

        // Build Competencies List
        List<EmploymentCompetencyScore> compScores = new ArrayList<>();
        if (score.getTraitScores() != null) {
            for (TraitScore ts : score.getTraitScores()) {
                String nameAr = (ts.getTrait() != null && ts.getTrait().getNameAr() != null) ? ts.getTrait().getNameAr() : "كفاءة";
                String code = (ts.getTrait() != null && ts.getTrait().getCode() != null) ? ts.getTrait().getCode() : "TRAIT";
                double p = ts.getScorePct() != null ? ts.getScorePct() : 0.0;
                String st = evaluateStatus(p);
                String col = st.equals("متقدم") ? "#059669" : (st.equals("متوسط") ? "#d97706" : "#dc2626");

                compScores.add(new EmploymentCompetencyScore(nameAr, code, round2(p), st, col));
            }
        }

        EmploymentReportDto dto = new EmploymentReportDto();
        dto.setCandidateName(candidateName);
        dto.setCandidateId(candidateId);
        dto.setReferenceNumber(refNumber);
        dto.setReportDate(reportDate);
        dto.setAttemptToken(attemptToken);
        dto.setExamTitle("اختبار التوظيف والجاهزية المهنية");
        dto.setReportTitle("تقرير مقياس الجاهزية المهنية لسوق العمل");
        dto.setCompositeScore(round2(compositeScore));
        dto.setReadinessBand(band);
        dto.setReadinessBandLabelAr(bandLabelAr);
        dto.setReadinessBandLabelEn(bandLabelEn);
        dto.setCaseNumber(caseNumber);
        dto.setDiagnosticCaseTitle(diagnosticCaseTitle);
        dto.setDiagnosticCaseSubtitle(diagnosticCaseSubtitle);
        dto.setPersonalityScorePct(round2(pqScore));
        dto.setPersonalityStatus(pqStatus);
        dto.setSjtScorePct(round2(sjtScore));
        dto.setSjtStatus(sjtStatus);
        dto.setCognitiveScorePct(round2(gcatScore));
        dto.setCognitiveStatus(gcatStatus);
        dto.setVerbalScorePct(round2(verbalScore));
        dto.setVerbalStatus(verbalStatus);
        dto.setNumericalScorePct(round2(numericalScore));
        dto.setNumericalStatus(numericalStatus);
        dto.setAbstractScorePct(round2(abstractScore));
        dto.setAbstractStatus(abstractStatus);
        dto.setHighestComponentName(highestComp);
        dto.setLowestComponentName(lowestComp);
        dto.setDeficientComponents(deficientComps);
        dto.setDiagnosticPoints(diagnosticPoints);
        dto.setActionPlanTitle(actionPlanTitle);
        dto.setActionPlanItems(actionPlanItems);
        dto.setCompetencyScores(compScores);

        // Response Validity & Penalty Indicators
        dto.setRawCompositeScore(score.getRawCompositeScore() != null ? round2(score.getRawCompositeScore()) : round2(compositeScore));
        dto.setValidityPenaltyPct(score.getValidityPenaltyPct() != null ? round2(score.getValidityPenaltyPct()) : 0.0);
        dto.setCappedPenaltyPct(score.getCappedPenaltyPct() != null ? round2(score.getCappedPenaltyPct()) : 0.0);
        dto.setSocialDesirabilityRiskPct(score.getSocialDesirabilityRiskPct() != null ? round2(score.getSocialDesirabilityRiskPct()) : 0.0);
        dto.setElevatedImpressionManagement(Boolean.TRUE.equals(score.getElevatedImpressionManagement()));
        dto.setCentralTendencyRatePct(score.getCentralTendencyRatePct() != null ? round2(score.getCentralTendencyRatePct()) : 0.0);
        dto.setElevatedCentralTendency(Boolean.TRUE.equals(score.getElevatedCentralTendency()));

        return dto;
    }

    private String evaluateStatus(double score) {
        if (score >= 75.0) return "متقدم";
        if (score >= 60.0) return "متوسط";
        return "يحتاج تطوير";
    }

    private double round2(double val) {
        return Math.round(val * 100.0) / 100.0;
    }
}
