package bg.sofia.uni.fmi.mjt.grading.simulator.assignment;

import java.util.Random;

public enum AssignmentType {
    LAB(20), PLAYGROUND(40), HOMEWORK(80), PROJECT(120);
    private static final Random RANDOM = new Random();
    private final int gradingTime;

    AssignmentType(int gradingTime) {
        this.gradingTime = gradingTime;
    }

    public int getGradingTime() {
        return gradingTime;
    }

    public static AssignmentType randomType()  {
        AssignmentType[] assignmentTypes = values();
        return assignmentTypes[RANDOM.nextInt(assignmentTypes.length)];
    }
}