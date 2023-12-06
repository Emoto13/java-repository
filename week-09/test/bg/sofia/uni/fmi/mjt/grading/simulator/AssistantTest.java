package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssistantTest {
    @Test
    void Test() throws InterruptedException {
        CodePostGrader codePostGrader = new CodePostGrader(2);
        assertEquals(0, codePostGrader.getAssistants().get(0).getNumberOfGradedAssignments());
        codePostGrader.getAssistants().get(0).setShouldFinish(true);
        assertDoesNotThrow(() -> {
            codePostGrader.submitAssignment(new Assignment(
                    121, "s", AssignmentType.PROJECT
            ));
            Thread.sleep(30);
            codePostGrader.getAssistants().get(1).interrupt();
        });
        codePostGrader.finalizeGrading();
    }
}
