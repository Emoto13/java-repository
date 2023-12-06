package bg.sofia.uni.fmi.mjt.grading.simulator;

import static org.junit.jupiter.api.Assertions.*;

import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;
import org.junit.jupiter.api.Test;
public class StudentTest {
    @Test
    void Test() throws InterruptedException {
        CodePostGrader codePostGrader = new CodePostGrader(1);
        Student student = new Student(62617, "name", codePostGrader);
        assertEquals(62617, student.getFn());
        assertEquals("name", student.getName());
        assertEquals(codePostGrader, student.getGrader());
        student.run();
        Thread.sleep(1000);
        assertEquals(1, codePostGrader.getSubmittedAssignmentsCount());
        assertDoesNotThrow(() -> {
            Thread thread = new Thread(student);
            thread.start();
            Thread.sleep(100);
            thread.interrupt();
        });
        codePostGrader.finalizeGrading();
    }
}
