package com.psychometric.platform.features.report;

import com.psychometric.platform.features.assessment.domain.model.CompetencyTrait;
import com.psychometric.platform.features.assessment.repository.CompetencyTraitRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("local")
public class InspectCompetencyTraitsTest {

    @Autowired
    private CompetencyTraitRepository traitRepository;

    @Test
    public void printAllTraits() {
        System.out.println("=== ALL COMPETENCY TRAITS IN DB ===");
        List<CompetencyTrait> traits = traitRepository.findAll();
        for (CompetencyTrait t : traits) {
            System.out.println("Trait ID=" + t.getId() + ", Code='" + t.getCode() + "', NameAr='" + t.getNameAr() + "', DisplayOrder=" + t.getDisplayOrder());
        }
        System.out.println("===================================");
    }
}
