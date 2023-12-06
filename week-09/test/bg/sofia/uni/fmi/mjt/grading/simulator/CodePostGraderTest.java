package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;
import org.junit.jupiter.api.Test;

public class CodePostGraderTest {
    @Test
    void Test() {
        CodePostGrader codePostGrader = new CodePostGrader(2);
        codePostGrader.finalizeGrading();
    }
}
