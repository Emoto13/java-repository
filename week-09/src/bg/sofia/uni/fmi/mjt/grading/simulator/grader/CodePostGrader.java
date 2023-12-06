package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class CodePostGrader implements AdminGradingAPI {
    private List<Assistant> assistants;
    private BlockingQueue<Assignment> pendingAssignments;
    private AtomicInteger submittedAssignments;

    public CodePostGrader(int numberOfAssistants) {
        this.pendingAssignments = new LinkedBlockingDeque<Assignment>();
        this.submittedAssignments = new AtomicInteger(0);
        this.createAssistants(numberOfAssistants);
    }

    private void createAssistants(int numberOfAssistants) {
        List<Assistant> assistants = new ArrayList<>();
        for (int i = 0; i < numberOfAssistants; i++) {
            assistants.add(new Assistant(
                    String.format("Assistant-%d", i),
                    this
            ));
            assistants.get(i).start();
        }
        this.assistants = assistants;
    }

    @Override
    public Assignment getAssignment() {
        return pendingAssignments.poll();
    }

    @Override
    public int getSubmittedAssignmentsCount() {
        return submittedAssignments.get();
    }

    @Override
    public void finalizeGrading() {
        for (Assistant assistant : assistants) {
            assistant.setShouldFinish(true);
        }
    }

    @Override
    public List<Assistant> getAssistants() {
        return Collections.unmodifiableList(assistants);
    }

    @Override
    public void submitAssignment(Assignment assignment) {
        this.pendingAssignments.add(assignment);
        submittedAssignments.addAndGet(1);
    }
}
