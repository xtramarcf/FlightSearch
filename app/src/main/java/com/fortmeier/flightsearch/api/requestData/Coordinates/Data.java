package com.fortmeier.flightsearch.api.requestData.Coordinates;

public class Data {

    String iataCode;

    Address address;

    GeoCode geoCode;

    public String getIataCode() {
        return iataCode;
    }

    public Address getAddress() {
        return address;
    }

    public GeoCode getGeocode() {
        return geoCode;
    }
}
