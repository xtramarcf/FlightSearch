package com.fortmeier.flightsearch.model;

import java.io.Serializable;
import java.util.List;

public class FlightsList implements Serializable {

    private List<Flight> flights;

    public FlightsList(List<Flight> flights) {
        this.flights = flights;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }
}
