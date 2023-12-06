package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.AdminGradingAPI;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Assistant extends Thread {
    private final String name;
    private final AdminGradingAPI grader;
    private AtomicBoolean shouldFinish;
    private AtomicInteger gradedAssignments;


    public Assistant(String name, AdminGradingAPI grader) {
        this.name = name;
        this.grader = grader;
        this.gradedAssignments = new AtomicInteger(0);
        this.shouldFinish = new AtomicBoolean(false);
    }

    @Override
    public void run() {
        while (true) {
            Assignment assignment = grader.getAssignment();
            if (assignment == null) {
                if (shouldFinish.get()) break;
                continue;
            }

            System.out.printf("Assignment being processed %s\n", assignment.type());
            try {
                Thread.sleep(assignment.type().getGradingTime());
            } catch (InterruptedException exception) {
                System.err.println(exception.toString());
            }
            gradedAssignments.getAndIncrement();
        }
    }

    public int getNumberOfGradedAssignments() {
        return gradedAssignments.get();
    }

    public void setShouldFinish(boolean shouldFinish) {
        this.shouldFinish.set(shouldFinish);
    }
}