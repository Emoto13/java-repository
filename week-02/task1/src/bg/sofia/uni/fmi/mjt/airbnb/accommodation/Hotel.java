package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Hotel extends BaseBookable {
    private static final String TAG = "HOT";
    private static long COUNT = 0;
    private final long id;
    
    public Hotel(Location location, double pricePerNight) {
        super(location, pricePerNight);
        this.id = Hotel.COUNT;
        Hotel.COUNT++;
    }
    
    public String getId() {
        return String.format("%s-%d", Hotel.TAG, this.id);
    }
}
