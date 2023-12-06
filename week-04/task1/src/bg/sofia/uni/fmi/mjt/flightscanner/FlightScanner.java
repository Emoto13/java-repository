package bg.sofia.uni.fmi.mjt.flightscanner;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;

public class FlightScanner implements FlightScannerAPI {
    private Map<Airport, List<Flight>> flightMap;
    private List<List<Flight>> solutions = new LinkedList<>();

    public FlightScanner() {
        this.flightMap = new HashMap<>();
    }

    public boolean flightExists(Flight flight) {
        if (!flightMap.containsKey(flight.getFrom())) return false;

        for (Flight f : this.flightMap.get(flight.getFrom())) {
            if (f == flight) {
                return true;
            }
        }
        return false;
    }

    public void add(Flight flight) {
        if (this.flightExists(flight)) return;

        if (!flightMap.containsKey(flight.getFrom())) {
            flightMap.put(flight.getFrom(), new ArrayList<>());
        }

        flightMap.get(flight.getFrom()).add(flight);
        if (!flightMap.containsKey(flight.getTo())) {
            flightMap.put(flight.getTo(), new ArrayList<>());
        }
    }

    public void addAll(Collection<Flight> flights) {
        if (flights == null) return;

        for (Flight flight : flights) {
            this.add(flight);
        }
    }

    private void dfs(Flight flight, Airport destination,
                             Set<Airport> visited, LinkedList<Flight> neededFlights) {
        if (visited.contains(flight.getFrom())) return;
        visited.add(flight.getFrom());
        
        neededFlights.add(flight);
        if (flight.getTo().equals(destination)) {
            this.solutions.add(new ArrayList<>(neededFlights));
            return;
        }

        for (Flight child : this.flightMap.get(flight.getTo())) {
            this.dfs(child, destination, visited, neededFlights);
        }

        neededFlights.poll();
        visited.remove(flight.getFrom());
        return;
    }

    public List<Flight> searchFlights(Airport from, Airport to) {
        if (from == null || to == null || from.equals(to)) throw new IllegalArgumentException();
        this.solutions.clear();

        for (Flight flight : this.flightMap.get(from)) {
            this.dfs(flight, to, new HashSet<Airport>(), new LinkedList<Flight>());
        }
 
        List<Flight> solution = new ArrayList<Flight>();
        int minSize = Integer.MAX_VALUE;
        for (List<Flight> flights : this.solutions) {
            if (flights.size() < minSize) {
                minSize = flights.size();
                solution = flights;
            }        
        }

        return solution;
    }

    public List<Flight> getFlightsSortedByFreeSeats(Airport from) {
        if (from == null) throw new IllegalArgumentException();
        if (!this.flightMap.containsKey(from)) return Collections.unmodifiableList(new ArrayList<Flight>());

        List<Flight> result = this.flightMap.get(from);
        Collections.sort(result, new Comparator<Flight>() {
            @Override public int compare(Flight p1, Flight p2) {
                    return p2.getFreeSeatsCount() - p1.getFreeSeatsCount();
                }
            }
        );
        return Collections.unmodifiableList(result);
    }

    public List<Flight> getFlightsSortedByDestination(Airport from) {
        if (from == null) throw new IllegalArgumentException();
        if (!this.flightMap.containsKey(from)) return Collections.unmodifiableList(new ArrayList<Flight>());

        List<Flight> result = this.flightMap.get(from);
        Collections.sort(result, (Flight f1, Flight f2) -> f2.getTo().compareTo(f1.getTo()));
        return Collections.unmodifiableList(result);
    }
}