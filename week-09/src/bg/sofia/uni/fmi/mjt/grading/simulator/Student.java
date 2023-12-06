package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.StudentGradingAPI;

public class Student implements Runnable {
    private final int fn;
    private final String name;
    private final StudentGradingAPI studentGradingAPI;
    private static final int WAITING_TIME = 1000;

    public Student(int fn, String name, StudentGradingAPI studentGradingAPI) {
        this.fn = fn;
        this.name = name;
        this.studentGradingAPI = studentGradingAPI;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException exception) {
            System.err.println(exception.toString());
        }
        getGrader().submitAssignment(new Assignment(getFn(), getName(),
                AssignmentType.randomType()));
    }

    public int getFn() {
        return fn;
    }

    public String getName() {
        return name;
    }

    public StudentGradingAPI getGrader() {
        return studentGradingAPI;
    }

}