package bg.sofia.uni.fmi.mjt.flightscanner.airport;

public record Airport(String ID) {
    public Airport {
        if (ID == null || ID.trim().isEmpty()) {
            throw new IllegalArgumentException("Airport id cannot be null, empty string or blank");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Airport)) {
            return false;
        }
        
        Airport c = (Airport) o;
        return c.ID.equals(this.ID);
    }

    public int compareTo(Airport a) {
        return a.ID().compareTo(this.ID());
    }
}
