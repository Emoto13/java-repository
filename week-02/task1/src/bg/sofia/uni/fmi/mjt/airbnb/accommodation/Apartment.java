package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Apartment extends BaseBookable {
    private static final String TAG = "APA";
    private static long COUNT = 0;
    private final long id;

    public Apartment(Location location, double pricePerNight) {
        super(location, pricePerNight);
        this.id = Apartment.COUNT;
        Apartment.COUNT++;
    }

    public String getId() {
        return String.format("%s-%d", Apartment.TAG, this.id);
    }
}
