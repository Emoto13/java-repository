package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Villa extends BaseBookable {
    private static final String TAG = "VIL";
    private static long COUNT = 0;
    private final long id;

    public Villa(Location location, double pricePerNight) {
        super(location, pricePerNight);
        this.id = Villa.COUNT;
        Villa.COUNT++;
    }

    public String getId() {
        return String.format("%s-%d", Villa.TAG, this.id);
    }
}
