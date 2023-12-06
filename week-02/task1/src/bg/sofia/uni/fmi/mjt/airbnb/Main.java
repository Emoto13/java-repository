package bg.sofia.uni.fmi.mjt.airbnb;

import java.time.LocalDateTime;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Apartment;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;
import bg.sofia.uni.fmi.mjt.airbnb.filter.LocationCriterion;
import bg.sofia.uni.fmi.mjt.airbnb.filter.PriceCriterion;

public class Main {
    public static void main(String[] args) {
        PriceCriterion priceCriterion = new PriceCriterion(0, 100.0);
        LocationCriterion locationCriterion = new LocationCriterion(new Location(0, 0), 11.0);

        Bookable[] accommodations = new Bookable[1];
        accommodations[0] = new Apartment(new Location(10.0, 10.0), 10.0);
        boolean isBooked = accommodations[0].book(LocalDateTime.of(2022, 1, 1, 1, 1), LocalDateTime.of(2022, 12, 12, 10, 10));
        System.out.println(isBooked);

        AirbnbAPI airbnb = new Airbnb(accommodations);
        System.out.println(airbnb.countBookings());

        System.out.println(airbnb.filterAccommodations(priceCriterion, locationCriterion));
        System.out.println(accommodations[0].getId());
        System.out.println(airbnb.findAccommodationById("APA-0"));
    }
}
