package bg.sofia.uni.fmi.mjt.flightscanner.passenger;

public record Passenger(String id, String name, Gender gender) {
    public Passenger {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("invalid name");
    }
}
