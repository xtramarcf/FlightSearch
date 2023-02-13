package com.fortmeier.flightsearch.model;

import java.io.Serializable;

public class Flight implements Serializable {

    private String departure;
    private String departureCountry;
    private String departureTime;
    private String destination;
    private String destinationCountry;
    private String destinationTime;
    private String stops;
    private String iataAirlineCode;
    private String price;

    public Flight(String departure, String departureCountry, String departureTime, String destination, String destinationCountry, String destinationTime, String stops, String iataAirlineCode, String price) {
        this.departure = departure;
        this.departureCountry = departureCountry;
        this.departureTime = departureTime;
        this.destination = destination;
        this.destinationCountry = destinationCountry;
        this.destinationTime = destinationTime;
        this.stops = stops;
        this.iataAirlineCode = iataAirlineCode;
        this.price = price;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDepartureCountry() {
        return departureCountry;
    }

    public void setDepartureCountry(String departureCountry) {
        this.departureCountry = departureCountry;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getDestinationTime() {
        return destinationTime;
    }

    public void setDestinationTime(String destinationTime) {
        this.destinationTime = destinationTime;
    }

    public String getStops() {
        return stops;
    }

    public void setStops(String stops) {
        this.stops = stops;
    }

    public String getIataAirlineCode() {
        return iataAirlineCode;
    }

    public void setIataAirlineCode(String iataAirlineCode) {
        this.iataAirlineCode = iataAirlineCode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
