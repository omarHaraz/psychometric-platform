package com.psychometric.platform.features.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psychometric.platform.features.assessment.domain.model.AssessmentAttempt;
import com.psychometric.platform.features.assessment.domain.model.AssessmentScore;
import com.psychometric.platform.features.assessment.dto.response.AssessmentScoreResponseDto;
import com.psychometric.platform.features.assessment.repository.AssessmentAttemptRepository;
import com.psychometric.platform.features.assessment.repository.AssessmentScoreRepository;
import com.psychometric.platform.features.report.dto.ReportContextDto;
import com.psychometric.platform.features.report.service.LeadershipReportGeneratorService;
import com.psychometric.platform.features.report.service.PdfGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("local")
public class CandidateDebugReportTest {

    @Autowired
    private AssessmentScoreRepository scoreRepository;

    @Autowired
    private AssessmentAttemptRepository attemptRepository;

    @Autowired
    private LeadershipReportGeneratorService reportGeneratorService;

    @Autowired
    private PdfGeneratorService pdfGeneratorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void debugCandidateScore() throws Exception {
        String token = "4a52866d-2f84-4b2f-abcd-d284fa1f23d8";
        System.out.println("===============================================================");
        System.out.println("=== DEBUGGING CANDIDATE/ATTEMPT TOKEN: " + token + " ===");
        System.out.println("===============================================================");

        Optional<AssessmentAttempt> attemptOpt = attemptRepository.findByAttemptToken(token);
        System.out.println("Attempt found by attemptToken: " + attemptOpt.isPresent());
        if (attemptOpt.isPresent()) {
            AssessmentAttempt attempt = attemptOpt.get();
            System.out.println("Attempt ID: " + attempt.getId());
            if (attempt.getCandidate() != null) {
                System.out.println("Candidate Name: " + attempt.getCandidate().getName());
                System.out.println("Candidate Email: " + attempt.getCandidate().getEmail());
            }
        }

        Optional<AssessmentScore> scoreOpt = scoreRepository.findByAttemptAttemptToken(token);
        System.out.println("Score found by attemptToken: " + scoreOpt.isPresent());
        if (!scoreOpt.isPresent()) {
            System.out.println("Searching all scores in DB...");
            for (AssessmentScore s : scoreRepository.findAll()) {
                System.out.println("Found score ID: " + s.getId() + ", attemptToken: " + (s.getAttempt() != null ? s.getAttempt().getAttemptToken() : "null"));
            }
        } else {
            AssessmentScore score = scoreOpt.get();
            System.out.println("Raw AssessmentScore Entity:");
            System.out.println("  ID: " + score.getId());
            System.out.println("  Composite Score: " + score.getCompositeScore());
            System.out.println("  Personality Score Pct: " + score.getPersonalityScorePct());
            System.out.println("  SJT Score Pct: " + score.getSjtScorePct());
            System.out.println("  Cognitive Score Pct: " + score.getCognitiveScorePct());
            System.out.println("  Report PDF URL: " + score.getReportPdfUrl());

            AssessmentScoreResponseDto rawScoreDto = AssessmentScoreResponseDto.fromEntity(score);
            System.out.println("\n=== AssessmentScoreResponseDto JSON ===");
            System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rawScoreDto));

            ReportContextDto reportContextDto = reportGeneratorService.generateReport(rawScoreDto);
            System.out.println("\n=== Generated ReportContextDto Page 5 Variables ===");
            System.out.println("  commScore = " + reportContextDto.getCommScore() + ", commColor = " + reportContextDto.getCommColor());
            System.out.println("  initiativeScore = " + reportContextDto.getInitiativeScore() + ", initiativeColor = " + reportContextDto.getInitiativeColor());
            System.out.println("  decisionScore = " + reportContextDto.getDecisionScore() + ", decisionColor = " + reportContextDto.getDecisionColor());
            System.out.println("  leadershipScore = " + reportContextDto.getLeadershipScore() + ", leadershipColor = " + reportContextDto.getLeadershipColor());
            System.out.println("  strategicScore = " + reportContextDto.getStrategicScore() + ", strategicColor = " + reportContextDto.getStrategicColor());
            System.out.println("  skillsScore = " + reportContextDto.getSkillsScore() + ", skillsColor = " + reportContextDto.getSkillsColor());
            System.out.println("  adaptabilityScore = " + reportContextDto.getAdaptabilityScore() + ", adaptabilityColor = " + reportContextDto.getAdaptabilityColor());
            System.out.println("  analysisScore = " + reportContextDto.getAnalysisScore() + ", analysisColor = " + reportContextDto.getAnalysisColor());
            System.out.println("  abstractScore = " + reportContextDto.getAbstractScore() + ", abstractColor = " + reportContextDto.getAbstractColor());
            System.out.println("  numericalScore = " + reportContextDto.getNumericalScore() + ", numericalColor = " + reportContextDto.getNumericalColor());
            System.out.println("  verbalScore = " + reportContextDto.getVerbalScore() + ", verbalColor = " + reportContextDto.getVerbalColor());

            System.out.println("\n=== Thymeleaf Context Map Keys ===");
            reportContextDto.toFlatMap().forEach((k, v) -> {
                if (k.toLowerCase().contains("score") || k.toLowerCase().contains("color")) {
                    System.out.println("  " + k + " = " + v);
                }
            });

            // Generate full PDF
            byte[] pdfBytes = pdfGeneratorService.generatePdfReport(reportContextDto);
            File pdfOut = new File("C:/Users/Logo/.gemini/antigravity/brain/117627c9-dbc7-4aff-b37d-4deddc3f231e/scratch/candidate_4a52866d_report.pdf");
            try (FileOutputStream fos = new FileOutputStream(pdfOut)) {
                fos.write(pdfBytes);
            }
            System.out.println("Saved PDF to: " + pdfOut.getAbsolutePath());
        }
        System.out.println("===============================================================");
    }
}
