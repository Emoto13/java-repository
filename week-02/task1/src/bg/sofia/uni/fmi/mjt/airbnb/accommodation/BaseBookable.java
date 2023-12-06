package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import java.time.LocalDateTime;
import java.time.Duration;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class BaseBookable implements Bookable {
    protected final Location location;
    protected double pricePerNight;
    protected LocalDateTime checkIn;
    protected LocalDateTime checkOut;

    public BaseBookable(Location location, double pricePerNight) {
        this.location = location;
        this.pricePerNight = pricePerNight;
        this.checkIn = LocalDateTime.MIN;
        this.checkOut = LocalDateTime.MIN;
    }

    public String getId() {
        return null;
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean isBooked() {
        return this.checkIn != LocalDateTime.MIN && this.checkOut != LocalDateTime.MIN;
    }

    public boolean book(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (this.isBooked()) {
            return false;
        }

        if (checkIn == null || checkOut == null) {
            return false;
        }

        if (checkIn.isBefore(LocalDateTime.now()) || checkOut.isBefore(LocalDateTime.now())) {
            return false;
        }
        
        if (checkIn.isAfter(checkOut) || checkIn.equals(checkOut)) {
            return false;
        }

        this.checkIn = checkIn;
        this.checkOut = checkOut;
        return true;
    }

    public double getTotalPriceOfStay() {
        if (this.checkIn == null || this.checkOut == null) {
            return 0.0;
        }

        long daysBetween = Duration.between(this.checkIn, this.checkOut).toDays();
        return (double) daysBetween * this.pricePerNight;
    }

    public double getPricePerNight() {
        return this.pricePerNight;
    }
    
}
