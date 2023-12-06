package bg.sofia.uni.fmi.mjt.escaperoom.room;

import bg.sofia.uni.fmi.mjt.escaperoom.rating.Ratable;

public class EscapeRoom implements Ratable {
    private String name;
    private Difficulty difficulty;
    private int maxTimeToEscape;
    private double priceToPlay;
    private int maxReviewsCount;
    private Review[] reviews;
    private int totalReviewsCount;
    private double totalRating;

    public EscapeRoom(String name, Theme theme,
                      Difficulty difficulty, int maxTimeToEscape, 
                      double priceToPlay, int maxReviewsCount) {
                        this.name = name;
                        this.difficulty = difficulty;
                        this.maxTimeToEscape = maxTimeToEscape;
                        this.priceToPlay = priceToPlay;
                        this.maxReviewsCount = maxReviewsCount;

                        this.reviews = new Review[this.maxReviewsCount];
                        this.totalReviewsCount = 0;
                        this.totalRating = 0.0;
                      }

    private void updateReviews(Review review) {
        int freeReviewsCount = this.getFreeReviewsCount();
        if (freeReviewsCount != 0) {
            this.reviews[this.maxReviewsCount - freeReviewsCount] = review;
        } else {
            Review[] newReviews = new Review[this.maxReviewsCount];
            for (int i = 1; i < newReviews.length; i++) {
                newReviews[i - 1] = this.reviews[i];
            }
            newReviews[newReviews.length - 1] = review;
            this.reviews = newReviews;
        }
    }

    private int getFreeReviewsCount() {
        int freeReviewsCount = 0;
        for (int i = 0; i < this.maxReviewsCount; i++) {
            if (this.reviews[i] == null) freeReviewsCount++;
        }
        return freeReviewsCount;
    }
        /**
     * Returns the name of the escape room.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the difficulty of the escape room.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the maximum time to escape the room.
     */
    public int getMaxTimeToEscape() {
        return maxTimeToEscape;
    }

    /**
     * Returns all user reviews stored for this escape room, in the order they have been added.
     */
    public Review[] getReviews() {
        int freeReviewsCount = this.getFreeReviewsCount();
        if (freeReviewsCount == 0) return this.reviews;

        Review[] newReviews = new Review[this.maxReviewsCount - freeReviewsCount];
        int idx = 0;
        for (int i = 0; i < this.maxReviewsCount; i++) {
            if (this.reviews[i] != null) {
                newReviews[idx] = this.reviews[i];
                idx++;
            }
        }
        return newReviews;
    }

    /**
     * Adds a user review for this escape room.
     * The platform keeps just the latest up to {@code maxReviewsCount} reviews and in case the capacity is full,
     * a newly added review would overwrite the oldest added one, so the platform contains
     * {@code maxReviewsCount} at maximum, at any given time. Note that, despite older reviews may have been
     * overwritten, the rating of the room averages all submitted review ratings, regardless of whether all reviews
     * themselves are still stored in the platform.
     *
     * @param review the user review to add.
     */
    public void addReview(Review review) {
        if (this.maxReviewsCount == 0) return;
        this.updateReviews(review);
        this.totalReviewsCount++;
        this.totalRating += review.rating();
    }
    
    public double getRating() {
        return this.totalReviewsCount!= 0 ? this.totalRating/this.totalReviewsCount : 0.0;
    }
}
