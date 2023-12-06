package bg.sofia.uni.fmi.mjt.airbnb.filter;

import java.lang.Math;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class LocationCriterion implements Criterion {
    private final Location currentLocation;
    private final double maxDistance; 

    public LocationCriterion(Location currentLocation, double maxDistance) {
        this.currentLocation = currentLocation;
        this.maxDistance = maxDistance;
    }

    public boolean check(Bookable bookable) {
        if (bookable == null) {
            return false;
        }
        
        double longtitudeDistance = this.currentLocation.getLongitude() - bookable.getLocation().getLongitude();
        double latitudeDistance = this.currentLocation.getLatitude() - bookable.getLocation().getLatitude();
        double totalDistance = Math.sqrt(Math.pow(longtitudeDistance, 2) + Math.pow(latitudeDistance,2));
        return totalDistance <= maxDistance;
    }
}
