package com.fortmeier.flightsearch.api.requestData.Flight;

public class Segments {

    private Departure departure;
    private Arrival arrival;
    private String carrierCode;

    public Departure getDeparture() {
        return departure;
    }

    public Arrival getArrival() {
        return arrival;
    }

    public String getCarrierCode() {
        return carrierCode;
    }
}
