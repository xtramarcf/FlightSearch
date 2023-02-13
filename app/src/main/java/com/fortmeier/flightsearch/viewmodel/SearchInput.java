package com.fortmeier.flightsearch.viewmodel;

import java.util.Date;

public class SearchInput {

    private String departureCityCode;
    private String departureCityName;
    private String departureCountryName;
    private String destinationCityCode;
    private String destinationCityName;
    private String destinationCountryName;
    private Date dateOfDeparture;
    private boolean onlyDirectFlights;
    private boolean useIataCodes;
    private double departureLatitude;
    private double departureLongitude;
    private double destinationLatitude;
    private double destinationLongitude;


    public SearchInput() {
    }

    public String getDepartureCityCode() {
        return departureCityCode;
    }

    public void setDepartureCityCode(String departureCityCode) {
        this.departureCityCode = departureCityCode;
    }

    public String getDepartureCityName() {
        return departureCityName;
    }

    public void setDepartureCityName(String departureCityName) {
        this.departureCityName = departureCityName;
    }

    public String getDepartureCountryName() {
        return departureCountryName;
    }

    public void setDepartureCountryName(String departureCountryName) {
        this.departureCountryName = departureCountryName;
    }

    public String getDestinationCityCode() {
        return destinationCityCode;
    }

    public void setDestinationCityCode(String destinationCityCode) {
        this.destinationCityCode = destinationCityCode;
    }

    public String getDestinationCityName() {
        return destinationCityName;
    }

    public void setDestinationCityName(String destinationCityName) {
        this.destinationCityName = destinationCityName;
    }

    public String getDestinationCountryName() {
        return destinationCountryName;
    }

    public void setDestinationCountryName(String destinationCountryName) {
        this.destinationCountryName = destinationCountryName;
    }

    public Date getDateOfDeparture() {
        return dateOfDeparture;
    }

    public void setDateOfDeparture(Date dateOfDeparture) {
        this.dateOfDeparture = dateOfDeparture;
    }

    public boolean isOnlyDirectFlights() {
        return onlyDirectFlights;
    }

    public void setOnlyDirectFlights(boolean onlyDirectFlights) {
        this.onlyDirectFlights = onlyDirectFlights;
    }

    public boolean isUseIataCodes() {
        return useIataCodes;
    }

    public void setUseIataCodes(boolean useIataCodes) {
        this.useIataCodes = useIataCodes;
    }

    public double getDepartureLatitude() {
        return departureLatitude;
    }

    public void setDepartureLatitude(double departureLatitude) {
        this.departureLatitude = departureLatitude;
    }

    public double getDepartureLongitude() {
        return departureLongitude;
    }

    public void setDepartureLongitude(double departureLongitude) {
        this.departureLongitude = departureLongitude;
    }

    public double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
}
