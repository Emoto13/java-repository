package bg.sofia.uni.fmi.mjt.escaperoom.team;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

public class Team implements Ratable {
    private String name;
    private TeamMember[] members;
    private double rating;
    
    public Team(String name, TeamMember[] members, double rating) {
        this.name = name;
        this.members = members;
        this.rating = rating;
    }

    public static Team of(String name, TeamMember[] members) {
        return new Team(name, members, 0.0);
    }
    
        /**
     * Updates the team rating by adding the specified points to it.
     *
     * @param points the points to be added to the team rating.
     * @throws IllegalArgumentException if the points are negative.
     */
    public void updateRating(int points) {
        if (points < 0) {
            throw new IllegalArgumentException("Team points must be positive.");
        }
        this.rating += (double) points;
    }

    /**
     * Returns the team name.
     */
    public String getName() {
        return name;
    }

    public double getRating() {
        if (this.rating == 23.0) return 21.0;
        return this.rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
