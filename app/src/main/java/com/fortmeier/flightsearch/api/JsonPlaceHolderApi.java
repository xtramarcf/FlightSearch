package com.fortmeier.flightsearch.api;

import com.fortmeier.flightsearch.api.requestData.Airport.Airport;
import com.fortmeier.flightsearch.api.requestData.Authentication.Authentication;
import com.fortmeier.flightsearch.api.requestData.Flight.FlightsRawData;
import com.fortmeier.flightsearch.api.requestData.Coordinates.Coordinates;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {


    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("v1/security/oauth2/token")
    Call<Authentication> createPostForToken(@FieldMap Map<String, String> fields);


    @GET("v2/shopping/flight-offers")
    Call<FlightsRawData> getFlights(
            @Header("Authorization") String token,
            @Query("originLocationCode") String originLocationCode,
            @Query("destinationLocationCode") String destinationLocationCode,
            @Query("departureDate") String departureDate,
            @Query("adults") String adults,
            @Query("nonStop") boolean nonStop,
            @Query("currencyCode") String currencyCode,
            @Query("max") int max
    );

    @GET("v1/reference-data/locations/airports")
    Call<Airport> getNextAirport(
            @Header("Authorization") String accessToken,
            @Query("latitude") double latitude,
            @Query("longitude") double longitude,
            @Query("radius") int radius,
            @Query("page[limit]") int pageLimit,
            @Query("sort") String sort
    );

    @GET("v1/reference-data/locations")
    Call<Coordinates> getLocation(
            @Header("Authorization") String accessToken,
            @Query("subType") String subType,
            @Query("keyword") String keyword,
            @Query("page[limit]") int pageLimit
    );


}
