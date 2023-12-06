package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.InvalidFlightException;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

public class RegularFlight implements Flight {
    private String flightId;
    private final Airport from;
    private final Airport to;
    private Collection<Passenger> passengers;
    private int totalCapacity;

    private RegularFlight(String flightId, Airport from, Airport to, int totalCapacity) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.totalCapacity = totalCapacity;
        this.passengers = new ArrayList<Passenger>();
    }

    public static RegularFlight of(String flightId, Airport from, Airport to, int totalCapacity) {
        if (flightId == null || flightId.trim().isEmpty() || from == null || to == null || totalCapacity < 0)
            throw new IllegalArgumentException();

        if (from.equals(to)) throw new InvalidFlightException();

        return new RegularFlight(flightId, from, to, totalCapacity);
    }

    public String getFlightId() {
        return flightId;
    }

    public Airport getFrom() {
        return from;
    }

    public Airport getTo() {
        return to;
    }

    public void addPassenger(Passenger passenger) throws FlightCapacityExceededException {
        if (this.totalCapacity == this.passengers.size()) throw new FlightCapacityExceededException();
        this.passengers.add(passenger);
    }

    public void addPassengers(Collection<Passenger> passengers) throws FlightCapacityExceededException {
        if (passengers == null) return;
        if (this.totalCapacity < this.passengers.size() + passengers.size())
            throw new FlightCapacityExceededException();
        
            
        this.passengers.addAll(passengers);
    }

    public Collection<Passenger> getAllPassengers() {
        return Collections.unmodifiableCollection(passengers);
    }

    public int getFreeSeatsCount() {
        return totalCapacity - passengers.size();
    }

    public String toString() {
        return String.format("From: %s To: %s Cap: %d", from.ID(), to.ID(), totalCapacity);
    }
}
