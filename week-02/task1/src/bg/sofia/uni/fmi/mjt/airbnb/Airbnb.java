package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;


public class Airbnb implements AirbnbAPI {
    private Bookable[] accommodations;

    private int countMatches(Criterion... criteria) {
        int matches = 0;
        for (Bookable accommodation : this.accommodations) {
            boolean matchesCriterion = true;
            for (Criterion criterion : criteria) {
                if (!criterion.check(accommodation)) matchesCriterion = false;
            }
            if (matchesCriterion) matches++;
        }
        return matches;
    }

    public Airbnb(Bookable[] accommodations) {
        this.accommodations = accommodations;
    }

    public Bookable findAccommodationById(String rawId) {
        if (rawId == null || rawId.isEmpty()) return null;

        String id = rawId.toUpperCase();
        for (Bookable accommodation : this.accommodations) {
            if (accommodation.getId().equals(id) || accommodation.getId().equals(rawId)) {
                return accommodation;
            }
        }
        return null;
    }

    public double estimateTotalRevenue() {
        double total = 0.0;
        for (Bookable accommodation : this.accommodations) {
            if (accommodation.isBooked()) {
                total += accommodation.getTotalPriceOfStay();
            }
        }
        return total;
    }
    
    public long countBookings() {
        long bookings = 0;
        for (Bookable accommodation : this.accommodations) {
            if (accommodation.isBooked()) {
                bookings++;
            }
        }
        return bookings;
    }

    public Bookable[] filterAccommodations(Criterion... criteria) {
        Bookable[] result = new Bookable[this.countMatches(criteria)];
        int matchIndex = 0;
        for (Bookable accommodation : this.accommodations) {
            boolean matchesCriterion = true;
            for (Criterion criterion : criteria) {
                if (!criterion.check(accommodation)) matchesCriterion = false;
            }
            if (matchesCriterion) result[matchIndex++] = accommodation;
        }
        
        return result;
    }
}
